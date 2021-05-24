package com.vnapnic.common.service

import com.vnapnic.common.property.JwtProperty
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.security.sasl.AuthenticationException
import javax.xml.bind.DatatypeConverter

interface JWTService {
    fun generateJWT(accountId: String?, deviceId: String?): String?
    fun parseJWT(token: String?): Map<String, String>?
}

const val ACCOUNT_ID = "accountId"
const val DEVICE_ID = "deviceId"

const val REDIS_JWT_ID = "jwt:id"
const val REDIS_JWT = "jwt"
const val REDIS_JWT_DEVICE_ID = "jwt:device"
const val REDIS_JWT_ACCOUNT_ID = "jwt:account"

open class JWTServiceImpl : JWTService {

    private val log = LoggerFactory.getLogger(JWTServiceImpl::class.java)

    private val property = JwtProperty()

    @Autowired
    lateinit var redisService: RedisService

    override fun generateJWT(accountId: String?, deviceId: String?): String? {
        if (accountId == null || deviceId == null)
            throw AuthenticationException("Token is expired.")

        //The JWT signature algorithm we will be using to sign the token
        val signatureAlgorithm: SignatureAlgorithm = SignatureAlgorithm.HS256
        //We will sign our JWT with our ApiKey secret
        val apiKeySecretBytes: ByteArray = DatatypeConverter.parseBase64Binary(property.jwtPhase)
        val signingKey: Key = SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.jcaName)

        val nowMillis = System.currentTimeMillis()
        val now = Date(nowMillis)

        val jwtId = UUID.randomUUID().toString()
        val redisJwtIdKey = "$REDIS_JWT_ID:$jwtId"
        val redisJwtKey = "$REDIS_JWT:$jwtId"
        val redisJwtDeviceKey = "$REDIS_JWT_DEVICE_ID:$jwtId"
        val redisJwtAccountKey = "$REDIS_JWT_ACCOUNT_ID:$jwtId"

        val subject = "$accountId&$deviceId"
        val map = HashMap<String, Any>()
        map[ACCOUNT_ID] = accountId
        map[DEVICE_ID] = deviceId

        //Let's set the JWT Claims
        val jwt = Jwts.builder()
                .setId(jwtId)
                .setIssuedAt(now)
                .setSubject(subject)
                .setExpiration(Date(nowMillis + property.jwtTTL))
                .setIssuer(property.jwtIssuer)
                .setHeaderParam("Authorization", "JWT")
                .signWith(signatureAlgorithm, signingKey)
                .compact()

        // Remove previous token record
        redisService.gets(redisJwtIdKey)?.forEach { _ ->
            val id = redisService[redisJwtIdKey]
            if (id?.equals(jwtId) == true) {
                redisService.del(redisJwtIdKey)
                redisService.del(redisJwtKey)
                redisService.del(redisJwtDeviceKey)
            }
        }

        redisService[redisJwtIdKey] = jwtId
        redisService[redisJwtKey] = jwt
        redisService[redisJwtDeviceKey] = deviceId
        redisService[redisJwtAccountKey] = accountId
        redisService.expire(redisJwtKey, nowMillis + property.jwtTTL)

        return jwt
    }

    override fun parseJWT(token: String?): Map<String, String>? {
        try {
            //This line will throw an exception if it is not a signed JWS (as expected)
            val jwsClaims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(property.jwtPhase))
                    .parseClaimsJws(token)

            if (jwsClaims.header.getAlgorithm() != SignatureAlgorithm.HS256.value)
                throw AuthenticationException("Invalid JWT token." + jwsClaims.header.getAlgorithm())
            val claims = jwsClaims.body

            // Find record from redis
            val record: String? = redisService["$REDIS_JWT:${claims.id}"] as? String
            if (record == null || !token.equals(record)) {
                throw AuthenticationException("Token is expired.")
            }

            val maps = mutableMapOf<String, String>()
            maps[ACCOUNT_ID] = redisService["$REDIS_JWT_ACCOUNT_ID:${claims.id}"] as? String
                    ?: throw Exception("can't find account id")
            maps[DEVICE_ID] = redisService["$REDIS_JWT_DEVICE_ID:${claims.id}"] as? String
                    ?: throw Exception("can't find device id.")

            return maps
        } catch (e: SignatureException) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e)
        } catch (e: MalformedJwtException) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e)
        } catch (e: ExpiredJwtException) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e)
        } catch (e: UnsupportedJwtException) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e)
        } catch (e: IllegalArgumentException) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return null
    }
}
package com.vnapnic.common.service

import com.vnapnic.common.dto.AccountDTO
import com.vnapnic.common.property.ApplicationProperty
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.security.sasl.AuthenticationException
import javax.xml.bind.DatatypeConverter

interface JWTService {
    fun generateJWT(accountDTO: AccountDTO?): String?
    fun parseJWT(token: String?): String?
}

open class JWTServiceImpl : JWTService {

    private val log = LoggerFactory.getLogger(JWTServiceImpl::class.java)

    private val property = ApplicationProperty()

    @Autowired
    lateinit var redisService: RedisService

    override fun generateJWT(accountDTO: AccountDTO?): String? {

        //The JWT signature algorithm we will be using to sign the token
        val signatureAlgorithm: SignatureAlgorithm = SignatureAlgorithm.HS256
        val nowMillis = System.currentTimeMillis()
        val now = Date(nowMillis)

        //We will sign our JWT with our ApiKey secret
        val apiKeySecretBytes: ByteArray = DatatypeConverter.parseBase64Binary(property.jwtPhase)
        val signingKey: Key = SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.jcaName)

        val jwtId = UUID.randomUUID().toString()

        //Let's set the JWT Claims
        val builder = Jwts.builder()
                .setId(jwtId)
                .setIssuedAt(now)
                .setSubject(accountDTO?.id) // User id
                .setIssuer(property.jwtIssuer)
                .setHeaderParam("Authorization", "JWT")
                .signWith(signatureAlgorithm, signingKey)

        //Let's add the expiration
        if (property.jwtTTL ?: -1 >= 0) {
            val expMillis = nowMillis + (property.jwtTTL ?: 0)
            val exp = Date(expMillis)
            builder.setExpiration(exp)
        }

        val jwt = builder.compact()
        // Remove previous token record
        val redis = redisService.gets("account:tkn:${accountDTO?.id}")?.forEach { _ ->
            val id = redisService["account:tkn:${accountDTO?.id}"]
            if (id?.equals(jwtId) == false) {
                redisService.del("tkn:${id}")
            }
        }

        log.info("redisssssssssssssssssss: $redis")
        log.info("jwtIddddddddddddddddddd: $jwtId")

        accountDTO?.id?.let { id ->
            redisService["tkn:$jwtId"] = jwt
            redisService["account:tkn:$id"] = jwtId
            redisService.expire("tkn:$jwtId", nowMillis + (property.jwtTTL ?: 0))
        } ?: throw AuthenticationException("Token is expired.")

        return jwt
    }

    override fun parseJWT(token: String?): String? {
        //This line will throw an exception if it is not a signed JWS (as expected)
        val jwsClaims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(property.jwtPhase))
                .parseClaimsJws(token)

        if (jwsClaims.header.getAlgorithm() != SignatureAlgorithm.HS256.value)
            throw AuthenticationException("Invalid JWT token." + jwsClaims.header.getAlgorithm())
        val claims = jwsClaims.body

        log.info("jwtIddddddddddddddddddd: ${claims.id}")
        log.info("accountIddddddddddddddd: ${jwsClaims.body.subject}")

        // Find record from redis
        val record: String? = redisService["tkn:" + claims.id] as? String
        if (record == null || !token.equals(record)) {
            throw AuthenticationException("Token is expired.")
        }

        return jwsClaims.body.subject ?: throw AuthenticationException("Token is expired.")
    }

}
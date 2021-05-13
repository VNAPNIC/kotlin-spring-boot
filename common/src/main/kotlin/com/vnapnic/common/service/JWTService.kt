package com.vnapnic.common.service

import com.vnapnic.common.models.Account
import com.vnapnic.common.property.ApplicationProperty
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.security.sasl.AuthenticationException
import javax.xml.bind.DatatypeConverter

interface JWTService{
    fun generateJWT(account: Account?): String?
    fun parseJWT(token: String?): String?
}

@Service
class JWTServiceImpl : JWTService{

    private val log = LoggerFactory.getLogger(JWTServiceImpl::class.java)

    @Autowired
    lateinit var redisService: RedisService

    @Autowired
    lateinit var property: ApplicationProperty

    override fun generateJWT(account: Account?): String? {
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
                .setSubject(account?.id) // User id
                .setIssuer(property.jwtIssuer)
                .setHeaderParam("Authorization", "JWT")
                .signWith(signatureAlgorithm, signingKey)

        //Let's add the expiration
        if (property.jwtTTL >= 0) {
            val expMillis = nowMillis + property.jwtTTL
            val exp = Date(expMillis)
            builder.setExpiration(exp)
        }

        val jwt = builder.compact()
        // Remove previous token record
        redisService.hGetAll("account:tkn:${account?.id}")?.forEach {
            val id = redisService.get(it.key)
        }

        return builder.compact()
    }

    override fun parseJWT(token: String?): String? {
        //This line will throw an exception if it is not a signed JWS (as expected)
        val jwsClaims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(property.jwtPhase))
                .parseClaimsJws(token)

        if (jwsClaims.header.getAlgorithm() != SignatureAlgorithm.HS256.value)
            throw AuthenticationException("Invalid JWT token." + jwsClaims.header.getAlgorithm())
        val claims = jwsClaims.body

        // Find record from redis
        val record: String? = redisService["tkn:" + claims.id] as? String
        log.info("----------------------> $record")
        if (record == null || !token.equals(record)) {
            throw AuthenticationException("Token is expired.")
        }

        return record
    }

}
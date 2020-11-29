package com.vnapnic.user.security

import com.vnapnic.configuration.security.SecurityProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class JwtTokenProvider constructor(private var securityProperties: SecurityProperties) {

    /**
     * Generates a token from a principal object. Embed the refresh token in the jwt
     * so that a new jwt can be created
     */
    fun generateToken(userId: Long): String {
        val expiryDate = Instant.now().plusMillis(securityProperties.expiration)
        return Jwts.builder()
                .setSubject((userId).toString())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS512, securityProperties.secret)
                .compact()
    }

    /**
     * Returns the user id encapsulated within the token
     */
    fun getUserIdFromJWT(token: String): Long {
        val claims: Claims = Jwts.parser()
                .setSigningKey(securityProperties.secret)
                .parseClaimsJws(token)
                .body
        return claims.subject.toLong()
    }

    /**
     * Returns the token expiration date encapsulated within the token
     */
    fun getTokenExpiryFromJWT(token: String): Date {
        val claims: Claims = Jwts.parser()
                .setSigningKey(securityProperties.secret)
                .parseClaimsJws(token)
                .body
        return claims.expiration
    }

    /**
     * Return the jwt expiration for the client so that they can execute
     * the refresh token logic appropriately
     */
    fun getExpiryDuration(): Long {
        return securityProperties.expiration
    }
}
package com.vnapnic.user.security

import com.vnapnic.common.exception.InvalidTokenRequestException
import com.vnapnic.configuration.security.SecurityProperties
import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class JwtTokenValidator {

    @Autowired
    private lateinit var securityProperties: SecurityProperties

    /**
     * Validates if a token satisfies the following properties
     * - Signature is not malformed
     * - Token hasn't expired
     * - Token is supported
     * - Token has not recently been logged out.
     */
    fun validateToken(authToken: String): Boolean {
        try {
            Jwts.parser().setSigningKey(securityProperties.secret).parseClaimsJws(authToken)
        } catch (ex: SignatureException) {
            throw InvalidTokenRequestException("JWT", authToken, "Incorrect signature")
        } catch (ex: MalformedJwtException) {
            throw InvalidTokenRequestException("JWT", authToken, "Malformed jwt token")
        } catch (ex: ExpiredJwtException) {
            throw InvalidTokenRequestException("JWT", authToken, "Token expired. Refresh required")
        } catch (ex: UnsupportedJwtException) {
            throw InvalidTokenRequestException("JWT", authToken, "Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            throw InvalidTokenRequestException("JWT", authToken, "Illegal argument token")
        }
        return true
    }
}
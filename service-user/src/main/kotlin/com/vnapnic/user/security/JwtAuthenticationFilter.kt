package com.vnapnic.user.security

import com.vnapnic.configuration.security.SecurityProperties
import com.vnapnic.user.service.user.CustomUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter : OncePerRequestFilter() {

    @Autowired
    private lateinit var securityProperties: SecurityProperties

    @Autowired
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @Autowired
    private lateinit var jwtTokenValidator: JwtTokenValidator

    @Autowired
    private lateinit var customUserDetailsService: CustomUserDetailsService

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            val jwt: String? = getJwtFromRequest(request)
            jwt?.let {
                if (StringUtils.hasText(jwt) && jwtTokenValidator.validateToken(jwt)) {
                    val userId: Long = jwtTokenProvider.getUserIdFromJWT(jwt)
                    val userDetails: UserDetails = customUserDetailsService.loadUserById(userId)
                    val authentication = UsernamePasswordAuthenticationToken(userDetails, jwt, userDetails.authorities)
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        } catch (ex: Exception) {
            throw ex
        }

        filterChain.doFilter(request, response)
    }

    /**
     * Extract the token from the Authorization request header
     */
    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(securityProperties.header)
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(securityProperties.prefix)) {
            return bearerToken.replace(securityProperties.prefix, "")
        }
        return null
    }
}
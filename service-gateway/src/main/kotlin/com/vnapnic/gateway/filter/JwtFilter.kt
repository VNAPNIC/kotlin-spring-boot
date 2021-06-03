package com.vnapnic.gateway.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import com.vnapnic.common.entities.Response
import com.vnapnic.common.service.JWTService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException
import org.apache.http.HttpStatus
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class JwtFilter : ZuulFilter() {
    private val log = LoggerFactory.getLogger(JwtFilter::class.java)

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Autowired
    lateinit var jwtService: JWTService

    override fun shouldFilter(): Boolean {
        val context = RequestContext.getCurrentContext()
        val request = context.request
        val requestUrl = request.requestURL.toString()
        return requestUrl.indexOf("/auth/") <= 0
                && requestUrl.indexOf("/swagger-ui") <=0
                && requestUrl.indexOf("/api-docs") <=0
    }

    override fun filterType(): String = "pre"

    override fun filterOrder(): Int = 0

    override fun run(): Any? {
        val context = RequestContext.getCurrentContext()
        val request = context.request
        val mapper = ObjectMapper()

        val accessToken = request.getHeader("Authorization")
        if (accessToken.isNullOrEmpty() || !accessToken.startsWith("Bearer")) {
            log.warn("Missing Authorization header and token.")
            context.setSendZuulResponse(false)
            context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED
            context.responseBody = mapper.writeValueAsString(Response.unauthorized())
        } else {
            try {
                val token: String = accessToken.replace("Bearer ", "")
                log.info("Token - $token")
                jwtService.parseJWT(token)
            } catch (e: SignatureException) {
                log.info("Invalid JWT signature.");
                log.trace("Invalid JWT signature trace: {}", e)
                context.setSendZuulResponse(false)
                context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED
                context.responseBody = mapper.writeValueAsString(Response.unauthorized(message = "Invalid JWT signature: {$e}"))
            } catch (e: MalformedJwtException) {
                log.info("Invalid JWT token.");
                log.trace("Invalid JWT token trace: {}", e)
                context.setSendZuulResponse(false)
                context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED
                context.responseBody = mapper.writeValueAsString(Response.unauthorized(message = "Invalid JWT token: {$e}"))
            } catch (e: ExpiredJwtException) {
                log.info("Expired JWT token.");
                log.trace("Expired JWT token trace: {}", e)
                context.setSendZuulResponse(false)
                context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED
                context.responseBody = mapper.writeValueAsString(Response.unauthorized(message = "Expired JWT token: {$e}"))
            } catch (e: UnsupportedJwtException) {
                log.info("Unsupported JWT token.");
                log.trace("Unsupported JWT token trace: {}", e)
                context.setSendZuulResponse(false)
                context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED
                context.responseBody = mapper.writeValueAsString(Response.unauthorized(message = "Unsupported JWT token: {$e}"))
            } catch (e: IllegalArgumentException) {
                log.info("JWT token compact of handler are invalid.");
                log.trace("JWT token compact of handler are invalid trace: {}", e)
                context.setSendZuulResponse(false)
                context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED
                context.responseBody = mapper.writeValueAsString(Response.unauthorized(message = "JWT token compact of handler are invalid trace: {$e}"))
            } catch (e: Exception) {
                e.printStackTrace()
                log.warn("Can't parse JWT token.")
                context.setSendZuulResponse(false)
                context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED
                context.responseBody = mapper.writeValueAsString(Response.unauthorized())
            }
        }

        log.info(String.format("%s request to %s", request.method, request.requestURL.toString()))
        return null
    }
}
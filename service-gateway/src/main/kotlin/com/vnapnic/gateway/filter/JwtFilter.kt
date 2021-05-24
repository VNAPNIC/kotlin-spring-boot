package com.vnapnic.gateway.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import com.vnapnic.common.beans.Response
import com.vnapnic.common.service.JWTService
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
    }

    override fun filterType(): String = "pre"

    override fun filterOrder(): Int = 0

    override fun run(): Any? {
        val context = RequestContext.getCurrentContext()
        val request = context.request
        log.info(String.format("%s request to %s", request.method, request.requestURL.toString()))

        val accessToken = request.getHeader("Authorization")
        if (accessToken == null || !accessToken.startsWith("Bearer")) {
            log.warn("Missing Authorization header and token.")
            context.setSendZuulResponse(false)
            context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED

            try {
                val mapper = ObjectMapper()
                context.responseBody = mapper.writeValueAsString(Response.unauthorized())
            } catch (e: Exception) {
                e.printStackTrace()
                log.error("Cannot validate token: $accessToken from ${request.requestURL}")
            }
        } else {
            try {
                log.info("Token - $accessToken")
                val token: String = accessToken.substring(7, accessToken.length)
                jwtService.parseJWT(token)
            } catch (e: Exception) {
                e.printStackTrace()
                context.setSendZuulResponse(false)
                context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED
                val mapper = ObjectMapper()
                context.responseBody = mapper.writeValueAsString(Response.unauthorized())
            }
        }
        return null
    }
}
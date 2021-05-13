package com.vnapnic.gateway.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import com.vnapnic.common.models.Response
import com.vnapnic.common.service.RedisService
import com.vnapnic.gateway.property.ApplicationProperty
import org.apache.http.HttpStatus
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class JwtFilter : ZuulFilter() {
    private val log = LoggerFactory.getLogger(JwtFilter::class.java)

    @Autowired
    lateinit var redisService: RedisService

    @Autowired
    lateinit var property: ApplicationProperty

    override fun shouldFilter(): Boolean {
        val context = RequestContext.getCurrentContext()
        val request = context.request
        val requestUrl = request.requestURL.toString()
        return requestUrl.contains("/auth/")
    }

    override fun filterType(): String = "pre"

    override fun filterOrder(): Int = 0

    override fun run(): Any? {
        val context = RequestContext.getCurrentContext()
        val request = context.request
        log.info(String.format("%s request to %s", request.method, request.requestURL.toString()))

        val accessToken = request.getHeader("Authorization")
        if (accessToken == null || accessToken.startsWith("Bearer ")) {
            log.warn("Missing Authorization header and token.")
            context.setSendZuulResponse(false)
            context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED

            try {
                val mapper = ObjectMapper()
                context.responseBody = mapper.writeValueAsString(Response.unauthorized<Any>())
            } catch (e: Exception) {
            }
            return null
        }
        log.debug(property.authServiceUrl)
        return null
    }
}
package com.vnapnic.gateway

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.apache.http.HttpStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class JwtFilter : ZuulFilter() {
    private val log = LoggerFactory.getLogger(JwtFilter::class.java)

    override fun shouldFilter(): Boolean = true

    override fun filterType(): String = "pre"

    override fun filterOrder(): Int = 0

    override fun run(): Any? {
            val context = RequestContext.getCurrentContext()
            val request = context.request
            log.info(String.format("%s request to %s", request.method, request.requestURL.toString()))
            if (request.requestURL.indexOf("/auth/") <= 0) {
                val authHeader = request.getHeader("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    log.warn("Missing Authorization header and token.")
                    context.setSendZuulResponse(false)
                    context.responseStatusCode = HttpStatus.SC_UNAUTHORIZED
                    return null
                }
            }
        return null
    }
}
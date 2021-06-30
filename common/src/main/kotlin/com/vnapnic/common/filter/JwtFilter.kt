package com.vnapnic.common.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import com.netflix.zuul.exception.ZuulException
import com.vnapnic.common.entities.Response
import com.vnapnic.common.service.JWTService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import kotlin.jvm.Throws


@Component
class JwtFilter : ZuulFilter() {
    private val log = LoggerFactory.getLogger(JwtFilter::class.java)

    @Autowired
    lateinit var jwtService: JWTService

    override fun shouldFilter(): Boolean {
        val context = RequestContext.getCurrentContext()
        val request = context.request
        val requestUrl = request.requestURL.toString()
        return requestUrl.indexOf("/register") <= 0 &&
                requestUrl.indexOf("/login") <= 0 &&
                requestUrl.indexOf("/verify") <= 0
    }

    /**
     * pre: can be called before the request is routed
     * routing: is called when routing request
     * post: Called after routing and error filters
     * error: Called when an error occurs while processing a request
     */
    override fun filterType(): String = "pre"

    override fun filterOrder(): Int = 1

    @Throws(ZuulException::class)
    override fun run(): Any? {
        val context = RequestContext.getCurrentContext()
        val request = context.request
        val uri: String = request.requestURI



        val accessToken = request.getHeader("Authorization")
        if (accessToken.isNullOrEmpty() || !accessToken.startsWith("Bearer")) {
            log.warn("Missing Authorization header and token.")
            dataBack(uri, "Missing Authorization header and token", null)
        } else {
            try {
                val token: String? = accessToken.replace("Bearer ", "")
                log.info("Token - $token")
                jwtService.parseJWT(token)
            } catch (e: SignatureException) {
                log.info("Invalid JWT signature.")
                log.trace("Invalid JWT signature trace: {}", e)
                dataBack(uri, "Invalid JWT signature", e)
            } catch (e: MalformedJwtException) {
                log.info("Invalid JWT token.")
                log.trace("Invalid JWT token trace: {}", e)
                dataBack(uri, "Invalid JWT token", e)
            } catch (e: ExpiredJwtException) {
                log.info("Expired JWT token.")
                log.trace("Expired JWT token trace: {}", e)
                dataBack(uri, "Expired JWT token", e)
            } catch (e: UnsupportedJwtException) {
                log.info("Unsupported JWT token.")
                log.trace("Unsupported JWT token trace: {}", e)
                dataBack(uri, "Unsupported JWT token", e)
            } catch (e: IllegalArgumentException) {
                log.info("JWT token compact of handler are invalid.")
                log.trace("JWT token compact of handler are invalid trace: {}", e)
                dataBack(uri, "JWT token compact of handler are invalid trace", e)
            } catch (e: Exception) {
                e.printStackTrace()
                log.warn("Can't parse JWT token.")
                dataBack(uri, null, e)
            }
        }
        return null
    }

    private fun dataBack(uri: String, result: String?, e: Exception?) {
        val mapper = ObjectMapper()

        val context = RequestContext.getCurrentContext()
        context.responseBody = result
        context.setSendZuulResponse(false)
        if (result != null)
            if (e != null) {
                context.responseBody = mapper.writeValueAsString(Response.unauthorized(message = "$result: {$e}"))
            } else {
                context.responseBody = mapper.writeValueAsString(Response.unauthorized(message = "$result."))
            }
        else
            context.responseBody = mapper.writeValueAsString(Response.unauthorized())
        log.info("<< [Return response] - url: {}, body: {}", uri, result)
    }

    private fun getClientIpAddress(request: HttpServletRequest): String? {
        var ip = request.getHeader("X-Forwarded-For")
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("Proxy-Client-IP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("WL-Proxy-Client-IP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("HTTP_CLIENT_IP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.remoteAddr
        }
        return ip
    }
}
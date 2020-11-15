package com.vnapnic.user.configuration.jwt

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationEntryPoint(
        @Qualifier("handlerExceptionResolver") private val resolver: HandlerExceptionResolver
) : AuthenticationEntryPoint {

    @Throws(IOException::class)
    override fun commence(request: HttpServletRequest, httpServletResponse: HttpServletResponse, ex: AuthenticationException) {
        if (request.getAttribute("javax.servlet.error.exception") != null) {
            val throwable = request.getAttribute("javax.servlet.error.exception") as Throwable
            resolver.resolveException(request, httpServletResponse, null, (throwable as Exception))
        }
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.message)
    }
}
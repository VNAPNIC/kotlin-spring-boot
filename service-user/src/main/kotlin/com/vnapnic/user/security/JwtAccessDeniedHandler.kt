package com.vnapnic.user.security

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAccessDeniedHandler : AccessDeniedHandler {
    /**
     * When a user tries to access a REST resource that requires permission, but the permission is insufficient,
     * This method will be called to send a 403 response and error message
     */
    @Throws(IOException::class)
    override fun handle(request: HttpServletRequest, response: HttpServletResponse, accessDeniedException: AccessDeniedException) {
        val exception = AccessDeniedException("Sorry you don not enough permissions to access it!")
        response.sendError(HttpServletResponse.SC_FORBIDDEN, exception.message)
    }
}
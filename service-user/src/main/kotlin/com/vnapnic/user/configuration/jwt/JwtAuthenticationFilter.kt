package com.vnapnic.user.configuration.jwt

import com.vnapnic.user.model.jwt.JwtConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter : OncePerRequestFilter(){

    @Autowired
    private lateinit var jwtConfig : JwtConfig

    @Autowired
    private lateinit var jwtTokenProvider: JwtTokenProvider

    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  filterChain: FilterChain
    ) {
        try {
//            val jwt = getJwtFromRequest(request)

        }catch (ex: Exception){
            throw ex
        }
    }
}
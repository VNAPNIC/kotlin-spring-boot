package com.vnapnic.user.configuration.jwt

import com.vnapnic.user.model.jwt.JwtConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class JwtTokenProvider {

    @Autowired
    private lateinit var jwtConfig: JwtConfig


}
package com.vnapnic.user.model.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "jwt-config")
data class JwtConfig(
        val prefix: String,
        val header: String,
        val secret: String,
        val expiration: Int
)
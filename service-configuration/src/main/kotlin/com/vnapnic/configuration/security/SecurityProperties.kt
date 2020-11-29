package com.vnapnic.configuration.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "jwt-security")
data class SecurityProperties(
        val prefix: String,
        val header: String,
        val secret: String,
        val expiration: Long
)
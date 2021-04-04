package com.vnapnic.auth.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "application")
internal class ApplicationProperty {
    var jwtTTL: Long = 0
    var jwtPhase: String? = null
    var jwtIssuer: String? = null
}

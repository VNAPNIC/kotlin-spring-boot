package com.vnapnic.common.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "application")
class ApplicationProperty {
    var jwtTTL: Long = 0
    var jwtPhase: String? = null
    var jwtIssuer: String? = null
}

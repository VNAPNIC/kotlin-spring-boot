package com.vnapnic.gateway.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "application")
class ApplicationProperty {
    var authServiceUrl: String? = null
}
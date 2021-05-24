package com.vnapnic.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "vnapnic")
class ApplicationProperties {
   lateinit var local: Local
    data class Local(
            var address: String? = null,
            var storagePath: String? = null
    )
}
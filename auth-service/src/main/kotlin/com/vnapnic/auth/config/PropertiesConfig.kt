package com.vnapnic.auth.config

import com.vnapnic.common.property.ApplicationProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(ApplicationProperty::class)
class PropertiesConfig {
}

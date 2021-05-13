package com.vnapnic.gateway

import com.vnapnic.gateway.property.ApplicationProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(ApplicationProperty::class)
class PropertiesConfig {}
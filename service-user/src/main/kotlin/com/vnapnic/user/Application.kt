package com.vnapnic.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableDiscoveryClient
@SpringBootApplication
@EnableWebSecurity
@ConfigurationPropertiesScan
@EnableCaching
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
package com.vnapnic.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableWebSecurity
@ConfigurationPropertiesScan
@EnableCaching
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}

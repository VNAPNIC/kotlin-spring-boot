package com.vnapnic.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
@EnableDiscoveryClient
@ConfigurationPropertiesScan
class AuthApplication

fun main(args: Array<String>) {
    runApplication<AuthApplication>(*args)
}

@Bean
fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
}
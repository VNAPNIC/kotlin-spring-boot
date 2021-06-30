package com.vnapnic.auth

import com.vnapnic.common.filter.JwtFilter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@EnableZuulProxy
@EnableEurekaClient
@ConfigurationPropertiesScan
@SpringBootApplication
class AuthApplication{
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun jwtFilter() : JwtFilter = JwtFilter()
}

fun main(args: Array<String>) {
    runApplication<AuthApplication>(*args)
}
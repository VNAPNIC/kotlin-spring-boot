package com.vnapnic.common.config

import com.vnapnic.common.service.JWTService
import com.vnapnic.common.service.JWTServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

open class BaseJWTConfig {
    @Bean
    open fun jwtService(): JWTService = JWTServiceImpl()
}
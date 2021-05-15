package com.vnapnic.gateway.config

import com.vnapnic.common.config.BaseJWTConfig
import com.vnapnic.common.service.JWTService
import com.vnapnic.common.service.JWTServiceImpl
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableCaching
@Configuration
class JWTConfig : BaseJWTConfig()
package com.vnapnic.auth.config

import com.vnapnic.common.config.BaseJWTConfig
import com.vnapnic.common.service.JWTService
import com.vnapnic.common.service.JWTServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JWTConfig : BaseJWTConfig()
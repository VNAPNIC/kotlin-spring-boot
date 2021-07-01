package com.vnapnic.p2p.config

import com.vnapnic.common.config.BaseJWTConfig
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration

@EnableCaching
@Configuration
class JwtConfig : BaseJWTConfig()
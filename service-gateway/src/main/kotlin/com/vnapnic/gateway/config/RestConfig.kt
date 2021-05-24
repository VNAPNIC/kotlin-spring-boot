package com.vnapnic.gateway.config

import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@EnableCaching
class RestConfig{
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}
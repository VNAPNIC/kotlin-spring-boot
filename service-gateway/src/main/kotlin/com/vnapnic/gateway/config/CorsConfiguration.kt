package com.vnapnic.gateway.config

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Collections

@Configuration
class CorsConfiguration {
    @Bean
    fun corsWebFilter(): CorsWebFilter {
        val corsConfig = CorsConfiguration()
        corsConfig.allowedOrigins = Collections.singletonList("*")
        corsConfig.maxAge = 3600L
        corsConfig.allowedMethods = listOf("OPTIONS", "HEAD", "GET", "POST", "PUT", "DELETE")
        corsConfig.addAllowedHeader("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)
        return CorsWebFilter(source)
    }
}
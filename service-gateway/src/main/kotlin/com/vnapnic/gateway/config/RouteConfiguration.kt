package com.vnapnic.gateway.config

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RouteConfiguration {
    @Bean
    fun initRouteConfiguration(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
                .route { rs ->
                    rs.path("/auth/**").uri("lb://AUTH-SERVICE/")
                }
                .route { rs: PredicateSpec ->
                    rs.path("/storage/**").uri("lb://STORAGE-SERVICE/")
                }
                .route { rs ->
                    rs.path("/user/**").uri("lb://USER-SERVICE/")
                }
                .route { rs ->
                    rs.path("/p2p/**").uri("lb://P2P-SERVICE/")
                }
                .build()
    }
}
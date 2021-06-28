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
                    rs.path("/api/auth/**")
                            .filters { f -> f.rewritePath("^/api/auth", "") }
                            .uri("lb://AUTH-SERVICE/")
                }
                .route { rs: PredicateSpec ->
                    rs.path("/api/storage/**")
                            .filters { f -> f.rewritePath("^/api/storage", "") }
                            .uri("lb://STORAGE-SERVICE/")
                }
                .route { rs ->
                    rs.path("/api/user/**")
                            .filters { f -> f.rewritePath("^/api/user", "") }
                            .uri("lb://USER-SERVICE/")
                }
                .route { rs ->
                    rs.path("/api/p2p/**")
                            .filters { f -> f.rewritePath("^/api/p2p", "") }
                            .uri("lb://P2P-SERVICE/")
                }
                .route { rs ->
                    rs.path("/ws/p2p/**")
                            .filters { f -> f.rewritePath("^/ws/p2p", "") }
                            .uri("lb:ws://P2P-SERVICE/")
                }
                .build()
    }
}
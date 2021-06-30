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
                    rs.path("/api/**")
                            .filters { f -> f.rewritePath("^/api", "") }
                            .uri("lb://AUTH-SERVICE/")
                }
                .route { rs: PredicateSpec ->
                    rs.path("/api/**")
                            .filters { f -> f.rewritePath("^/api", "") }
                            .uri("lb://STORAGE-SERVICE/")
                }
                .route { rs ->
                    rs.path("/api/**")
                            .filters { f -> f.rewritePath("^/api", "") }
                            .uri("lb://USER-SERVICE/")
                }
                .route { rs ->
                    rs.path("/api/**")
                            .filters { f -> f.rewritePath("^/api", "") }
                            .uri("lb://P2P-SERVICE/")
                }
                .route { rs ->
                    rs.path("/ws/**")
                            .filters { f -> f.rewritePath("^/ws", "") }
                            .uri("lb:ws://P2P-SERVICE/")
                }
                .build()
    }
}
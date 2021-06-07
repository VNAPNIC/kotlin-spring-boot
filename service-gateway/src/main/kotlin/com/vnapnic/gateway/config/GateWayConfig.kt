package com.vnapnic.gateway.config

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GateWayConfig {
//    @Bean
//    fun customRouteLocator(routeLocatorBuilder: RouteLocatorBuilder): RouteLocator {
//        val routes = routeLocatorBuilder.routes()
//        routes.route("auth-service") { r: PredicateSpec ->
//            r.path("/api/auth/*").uri("lb://auth-service")
//        }.route("storage-service"){ r: PredicateSpec ->
//            r.path("/api/storage/*").uri("lb://storage-service")
//        }.route("user-service"){ r: PredicateSpec ->
//            r.path("/api/user/*").uri("lb://user-service")
//        }.route("p2p-service"){ r: PredicateSpec ->
//            r.path("/api/p2p/*").uri("lb://p2p-service")
//        }.build()
//        return routes.build()
//    }
}
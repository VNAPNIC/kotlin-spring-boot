package com.vnapnic.gateway

import com.vnapnic.gateway.filter.JwtFilter
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@EnableHystrix
class GatewayApplication

@Bean
fun jwtFilter(): JwtFilter {
    return JwtFilter()
}

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}



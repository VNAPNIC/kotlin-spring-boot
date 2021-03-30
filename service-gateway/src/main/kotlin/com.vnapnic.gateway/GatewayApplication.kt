package com.vnapnic.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.zuul.EnableZuulProxy

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
class GatewayApplication

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}
package com.vnapnic.p2p

import com.vnapnic.common.filter.JwtFilter
import com.vnapnic.p2p.services.ParserImpl
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate
import org.springframework.web.socket.config.annotation.EnableWebSocket

@EnableZuulProxy
@EnableEurekaClient
@ConfigurationPropertiesScan
@SpringBootApplication
@EnableWebSocket
class P2pApplication{

    @Bean
    fun parser() = ParserImpl()

    @Bean
    @LoadBalanced
    fun restTemplate(): RestTemplate? {
        return RestTemplate()
    }

    @Bean
    fun jwtFilter() : JwtFilter = JwtFilter()
}

fun main(args: Array<String>) {
    runApplication<P2pApplication>(*args)
}
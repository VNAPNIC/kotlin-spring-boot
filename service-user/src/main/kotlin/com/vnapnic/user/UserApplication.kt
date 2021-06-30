package com.vnapnic.user

import com.vnapnic.common.filter.JwtFilter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@EnableZuulProxy
@EnableEurekaClient
@ConfigurationPropertiesScan
@SpringBootApplication
class UserApplication{
    @Bean
    @LoadBalanced
    fun restTemplate(): RestTemplate? {
        return RestTemplate()
    }

    @Bean
    fun jwtFilter() : JwtFilter = JwtFilter()
}

fun main(args: Array<String>) {
    runApplication<UserApplication>(*args)
}
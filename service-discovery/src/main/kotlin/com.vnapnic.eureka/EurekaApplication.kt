package com.vnapnic.eureka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient
class EurekaApplication

fun main(args: Array<String>) {
    runApplication<EurekaApplication>(*args)
}
package com.vnapnic.eureka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableEurekaServer
class EurekaApplication

fun main(args: Array<String>) {
    runApplication<EurekaApplication>(*args)
}
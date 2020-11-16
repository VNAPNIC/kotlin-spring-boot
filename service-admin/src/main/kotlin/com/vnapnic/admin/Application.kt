package com.vnapnic.admin

import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
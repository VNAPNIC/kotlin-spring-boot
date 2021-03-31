package com.vnapnic.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication
class GroupApplication

fun main(args: Array<String>) {
    runApplication<GroupApplication>(*args)
}
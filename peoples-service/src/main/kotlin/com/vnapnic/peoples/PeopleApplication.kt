package com.vnapnic.peoples

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class PeopleApplication

fun main(args: Array<String>) {
    runApplication<PeopleApplication>(*args)
}
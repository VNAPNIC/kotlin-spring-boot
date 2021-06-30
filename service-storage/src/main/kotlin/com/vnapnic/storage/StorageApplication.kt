package com.vnapnic.storage

import com.vnapnic.common.filter.JwtFilter
import com.vnapnic.storage.services.FilesStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean

@EnableZuulProxy
@EnableEurekaClient
@ConfigurationPropertiesScan
@SpringBootApplication
class StorageApplication : CommandLineRunner {
    @Autowired
    lateinit var storageService: FilesStorageService

    override fun run(vararg args: String?) {
        storageService.init()
    }

    @Bean
    fun jwtFilter() : JwtFilter = JwtFilter()
}

fun main(args: Array<String>) {
    runApplication<StorageApplication>(*args)
}
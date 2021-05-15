package com.vnapnic.media

import com.vnapnic.media.services.FilesStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient


@SpringBootApplication
@EnableDiscoveryClient
class MediaApplication : CommandLineRunner {
    @Autowired
    lateinit var storageService: FilesStorageService

    override fun run(vararg args: String?) {
        storageService.init()
    }
}

fun main(args: Array<String>) {
    runApplication<MediaApplication>(*args)
}
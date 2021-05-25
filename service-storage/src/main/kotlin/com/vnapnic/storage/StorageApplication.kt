package com.vnapnic.storage

import com.vnapnic.storage.services.FilesStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class StorageApplication : CommandLineRunner {
    @Autowired
    lateinit var storageService: FilesStorageService

    override fun run(vararg args: String?) {
        storageService.init()
    }
}

fun main(args: Array<String>) {
    runApplication<StorageApplication>(*args)
}
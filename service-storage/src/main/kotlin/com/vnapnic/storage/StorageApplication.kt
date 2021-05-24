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

//    @Bean
//    fun multipartConfigElement(): MultipartConfigElement? {
//        val factory = MultipartConfigFactory()
//        factory.setMaxFileSize(DataSize.ofMegabytes(10L))
//        factory.setMaxRequestSize(DataSize.ofMegabytes(10L))
//        return factory.createMultipartConfig()
//    }
}

fun main(args: Array<String>) {
    runApplication<StorageApplication>(*args)
}
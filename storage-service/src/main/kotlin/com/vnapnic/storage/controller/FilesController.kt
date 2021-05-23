package com.vnapnic.storage.controller

import com.vnapnic.storage.services.FilesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class FilesController {

    @Autowired
    lateinit var filesService: FilesService

    @GetMapping("/fetch/{key:.+}")
    fun fetch(@PathVariable key: String) {

    }

    @GetMapping("/download/{key:.+}")
    fun download(@PathVariable key: String) {

    }
}
package com.vnapnic.peoples.controllers

import com.vnapnic.peoples.services.DemoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/people")
class DemoController(@Autowired private val demoService: DemoService) {

    @GetMapping("/hello")
    fun test(): String {
        return "Hello group"
    }
}
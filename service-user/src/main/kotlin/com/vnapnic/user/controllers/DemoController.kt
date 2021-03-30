package com.vnapnic.user.controllers

import com.vnapnic.user.services.DemoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class DemoController(@Autowired private val demoService: DemoService) {

    @RequestMapping("/test")
    fun test(): String {
        return demoService.test("aaaaaaaaaaaaaaaaaa")
    }
}
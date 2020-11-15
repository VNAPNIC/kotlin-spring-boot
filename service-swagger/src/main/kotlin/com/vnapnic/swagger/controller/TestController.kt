package com.vnapnic.swagger.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["用户相关接口"], description = "提供用户相关的 Rest API")
class TestController {

    @ApiOperation("新增用户接口")
    @GetMapping("/test")
    fun test(): String {
        return "test"
    }

    @GetMapping("/test/{id}")
    fun test(@PathVariable("id") id: String): String {
        return id
    }
}

package com.vnapnic.user.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @RequestMapping(value = ["/hello"], method = [RequestMethod.GET])
    fun hello() : String{
        throw Exception("aaaa")
    }
}

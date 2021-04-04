package com.vnapnic.auth.controllers

import io.jsonwebtoken.lang.Assert
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {

    @RequestMapping(value = ["/email"], method = [RequestMethod.POST])
    fun authWithEmail(@RequestBody json: Map<String,String>): String {
        Assert.isTrue(!json.isNullOrEmpty(), "Missing json.")
        Assert.isTrue(json.containsKey("email"), "Missing email.")
        Assert.isTrue(json.containsKey("password"), "Missing password.")

        val email = json["email"].toString()


        return "signin"
    }

    @RequestMapping(value = ["/facebook"], method = [RequestMethod.POST])
    fun authWithFacebook(@RequestBody json: Map<String,String>): String {
        return "facebook"
    }
}
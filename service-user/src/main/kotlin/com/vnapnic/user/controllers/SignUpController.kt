package com.vnapnic.user.controllers

import com.vnapnic.common.models.Response
import com.vnapnic.user.models.User
import com.vnapnic.user.services.SignUpService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/api/user")
class SignUpController(val service: SignUpService) {

    @PostMapping("/signup")
    @ResponseBody
    fun signUp(
            @RequestParam username: String,
            @RequestParam password: String,
            @RequestParam firstName: String,
            @RequestParam lastName: String,
            @RequestParam email: String
    ) : Response<User> {

        return Response<User>.failed<User>()
    }
}
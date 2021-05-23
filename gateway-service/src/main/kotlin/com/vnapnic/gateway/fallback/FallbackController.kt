package com.vnapnic.gateway.fallback

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class FallbackController {
    @RequestMapping("/authFallBack")
    fun authServiceFallBack() :String{
        return "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
    }

    @RequestMapping("/userFallBack")
    fun userServiceFallBack() :String{
        return "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"
    }
}
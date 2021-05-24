package com.vnapnic.user.controller

import com.vnapnic.common.beans.ErrorCode
import com.vnapnic.common.beans.Response
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

//    @RequestMapping(value = ["/update"], method = [RequestMethod.POST])
//    fun updateProfile(@RequestBody json: Map<String, String>) : Response{
//        try {
//            val userId: String? = json["userId"]
//            val firstName: String? = json["firstName"]
//            val lastName: String? = json["lastName"]
//            val weight: String? = json["weight"]
//            val height: String? = json["height"]
//            val gender: String? = json["gender"]
//            val description: String? = json["description"]
//
//
//
//        }catch (e: Exception){
//            e.printStackTrace()
//            return Response.failed(error = ErrorCode.SERVER_UNKNOWN_ERROR)
//        }
//    }
}

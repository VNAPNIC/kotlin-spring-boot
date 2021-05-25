package com.vnapnic.user.controller

import com.vnapnic.common.beans.ErrorCode
import com.vnapnic.common.beans.Response
import com.vnapnic.common.service.ACCOUNT_ID
import com.vnapnic.common.service.DEVICE_ID
import com.vnapnic.common.service.JWTService
import com.vnapnic.common.utils.JWTUtils
import com.vnapnic.database.enums.Gender
import com.vnapnic.database.exception.UserNotFound
import com.vnapnic.user.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*

@RestController
class UserController {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var jwtService: JWTService

    @RequestMapping(value = ["/update"], method = [RequestMethod.PUT])
    fun updateProfile(
            @RequestHeader headers: MultiValueMap<String, String>,
            @RequestBody json: Map<String, String>): Response {
        try {
            val acceptToken = JWTUtils.tokenFromBearerToken(headers["authorization"]?.get(0))
            val claims = jwtService.parseJWT(acceptToken)
            val accountId = claims?.get(ACCOUNT_ID)
            val deviceId = claims?.get(DEVICE_ID)
            if (accountId.isNullOrEmpty() || deviceId.isNullOrEmpty())
                return Response.failed(error = ErrorCode.USER_NOT_FOUND)

            val userId = userService.getUserIdByAccountId(accountId)
            val firstName: String? = json["firstName"]
            val lastName: String? = json["lastName"]
            val weight: Double? = json["weight"]?.toDouble()
            val height: Double? = json["height"]?.toDouble()
            val gender: String = json["gender"] ?: "OTHER"
            val description: String? = json["description"]

            if (userId.isNullOrEmpty())
                return Response.failed(error = ErrorCode.USER_NOT_FOUND)

            val dto = userService.updateProfile(
                    userId = userId,
                    firstName = firstName,
                    lastName = lastName,
                    weight = weight,
                    height = height,
                    gender = Gender.valueOf(gender),
                    description = description)

            return Response.success(data = dto)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ErrorCode.SERVER_UNKNOWN_ERROR)
        }
    }

    @RequestMapping(value = ["/{key:.+}"], method = [RequestMethod.GET])
    fun getUserInfo(@PathVariable key: String?): Response {
        if (key.isNullOrEmpty())
            return Response.failed(error = ErrorCode.USER_NOT_FOUND)
        return try {
            val dto = userService.findById(key)
            Response.success(data = dto)
        } catch (e: UserNotFound) {
            Response.failed(error = ErrorCode.USER_NOT_FOUND)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.failed(error = ErrorCode.SERVER_UNKNOWN_ERROR)
        }
    }
}

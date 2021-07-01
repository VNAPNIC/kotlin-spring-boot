package com.vnapnic.auth.controllers

import com.vnapnic.auth.dto.SignOutRequest
import com.vnapnic.auth.services.AuthService
import com.vnapnic.common.entities.Response
import com.vnapnic.common.entities.ResultCode
import com.vnapnic.common.exception.toToken
import com.vnapnic.common.service.JWTService
import com.vnapnic.database.redis.JWT
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import javax.security.sasl.AuthenticationException

@RestController
@RequestMapping("/logout")
class SignOutController {
    @Autowired
    lateinit var jwtService: JWTService

    @Autowired
    lateinit var authService: AuthService

    @RequestMapping(value = ["/logout"], method = [RequestMethod.POST])
    fun logout(@RequestHeader headers: MultiValueMap<String, String>, @RequestBody request: SignOutRequest?): Response<*> {
        try {
            val acceptToken = headers["authorization"]?.get(0)?.toToken()
            val claims = jwtService.parseJWT(acceptToken)
            val accountId = claims?.get(JWT.ACCOUNT_ID)
            val deviceId = claims?.get(JWT.DEVICE_ID)
            if (accountId.isNullOrEmpty() || deviceId.isNullOrEmpty())
                return Response.failed(error = ResultCode.USER_NOT_FOUND)
            authService.logout(accountId)
            jwtService.removeToken(acceptToken)
            return Response.success()
        } catch (e: AuthenticationException) {
            e.printStackTrace()
            return Response.failed()
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(ResultCode.SERVER_UNKNOWN_ERROR)
        }
    }
}
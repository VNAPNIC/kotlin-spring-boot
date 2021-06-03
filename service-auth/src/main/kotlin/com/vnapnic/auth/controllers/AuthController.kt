package com.vnapnic.auth.controllers

import com.vnapnic.auth.dto.AuthRequest
import com.vnapnic.auth.dto.AccountResponse
import com.vnapnic.auth.dto.test
import com.vnapnic.auth.services.AuthService
import com.vnapnic.database.enums.Platform
import com.vnapnic.common.entities.ErrorCode
import com.vnapnic.common.entities.Response
import com.vnapnic.common.service.JWTService
import com.vnapnic.common.utils.isEmail
import com.vnapnic.common.utils.isPhoneNumber
import com.vnapnic.database.entities.DeviceEntity
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.Authorization
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@Api(value = "/login", description = "Provider api authentication")
@RestController
@RequestMapping("/login")
class AuthController {
    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @Autowired
    lateinit var authService: AuthService

    @Autowired
    lateinit var jwtService: JWTService

    @RequestMapping(value = ["/email"], method = [RequestMethod.POST])
    @ApiOperation(
            value = "Login with email",
            response  = AccountResponse::class,
    )
    fun authWithEmail(@RequestBody request: AuthRequest?): Response<*> {
        try {

            if (request == null)
                return Response.failed(error = ErrorCode.WARNING_DATA_FORMAT)

            if (request.email.isNullOrEmpty())
                return Response.failed(error = ErrorCode.EMAIL_IS_NULL_BLANK)

            if (!request.email.isEmail())
                return Response.failed(error = ErrorCode.EMAIL_WRONG_FORMAT)

            if (request.password.isNullOrEmpty())
                return Response.failed(error = ErrorCode.PASSWORD_IS_NULL_BLANK)

            if (request.deviceId.isNullOrEmpty() || request.deviceName.isNullOrEmpty() || request.platform.isNullOrEmpty())
                return Response.failed(error = ErrorCode.UNSUPPORTED_DEVICE)

            log.info(String.format("request with %s %s", request.email, request.password))
            // Find account with same username, check password
            if (!authService.existsByEmail(request.email))
                return Response.failed(error = ErrorCode.EMAIL_PASSWORD_NOT_CORRECT)

            // get account by email
            val rawAccount = authService.findByEmail(request.email)
                    ?: return Response.failed(error = ErrorCode.EMAIL_PASSWORD_NOT_CORRECT)

            // Validate password
            if (!authService.validatePassword(request.password, rawAccount.password))
                return Response.failed(error = ErrorCode.EMAIL_PASSWORD_NOT_CORRECT)

            // Save device
            val device = authService.saveDevice(DeviceEntity(
                    deviceId = request.deviceId,
                    deviceName = request.deviceName,
                    platform = Platform.valueOf(request.platform)
            ))

            // Update device after login
            if (rawAccount.devices?.any { element -> element?.deviceId == request.deviceId } == false)
                rawAccount.devices?.add(device)

            val jwt = jwtService.generateJWT(rawAccount.id, request.deviceId)

            val dto = authService.login(rawAccount)
            return Response.success(data = dto, token = jwt)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ErrorCode.SERVER_UNKNOWN_ERROR)
        }
    }

    @RequestMapping(value = ["/phone"], method = [RequestMethod.POST])
    @ApiOperation(
            value = "Login with phone number",
            response = AccountResponse::class
    )
    fun authWithPhoneNumber(@RequestBody request: AuthRequest?): Response<*> {
        try {
            if (request == null)
                return Response.failed(error = ErrorCode.WARNING_DATA_FORMAT)

            if (request.phoneNumber.isNullOrEmpty())
                return Response.failed(error = ErrorCode.EMAIL_IS_NULL_BLANK)

            if (!request.phoneNumber.isPhoneNumber())
                return Response.failed(error = ErrorCode.PHONE_NUMBER_WRONG_FORMAT)

            if (request.password.isNullOrEmpty())
                return Response.failed(error = ErrorCode.PASSWORD_IS_NULL_BLANK)

            if (request.deviceId.isNullOrEmpty() || request.deviceName.isNullOrEmpty() || request.platform.isNullOrEmpty())
                return Response.failed(error = ErrorCode.UNSUPPORTED_DEVICE)

            log.info(String.format("request with %s %s", request.phoneNumber, request.password))

            // Find account with same username, check password
            if (!authService.existsByPhoneNumber(request.phoneNumber))
                return Response.failed(error = ErrorCode.PHONE_NUMBER_PASSWORD_NOT_CORRECT)

            // get account by phone number
            val rawAccount = authService.findByPhoneNumber(request.phoneNumber)
                    ?: return Response.failed(error = ErrorCode.PHONE_NUMBER_PASSWORD_NOT_CORRECT)

            // Validate password
            if (!authService.validatePassword(request.password, rawAccount.password))
                return Response.failed(error = ErrorCode.PHONE_NUMBER_PASSWORD_NOT_CORRECT)

            // Save device
            val device = authService.saveDevice(DeviceEntity(
                    deviceId = request.deviceId,
                    deviceName = request.deviceName,
                    platform = Platform.valueOf(request.platform)
            ))

            // Update device after login
            if (rawAccount.devices?.any { element -> element?.deviceId == request.deviceId } == false)
                rawAccount.devices?.add(device)

            val jwt = jwtService.generateJWT(rawAccount.id, request.deviceId)

            val dto = authService.login(rawAccount)
            return Response.success(data = dto, token = jwt)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ErrorCode.SERVER_UNKNOWN_ERROR)
        }
    }

    @RequestMapping(value = ["/facebook"], method = [RequestMethod.POST])
    fun authWithFacebook(@RequestBody json: Map<String, String>): String {
        return "facebook"
    }
}
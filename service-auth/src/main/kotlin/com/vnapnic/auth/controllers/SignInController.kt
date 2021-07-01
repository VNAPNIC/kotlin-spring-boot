package com.vnapnic.auth.controllers

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.vnapnic.auth.dto.SignInRequest
import com.vnapnic.common.dto.AccountResponse
import com.vnapnic.auth.services.AuthService
import com.vnapnic.database.enums.Platform
import com.vnapnic.common.entities.Response
import com.vnapnic.common.entities.ResultCode
import com.vnapnic.common.service.JWTService
import com.vnapnic.common.utils.isEmail
import com.vnapnic.database.entities.DeviceEntity
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@Api(value = "/login", description = "Provider api authentication")
@RestController
@RequestMapping("/login")
class SignInController {
    private val log = LoggerFactory.getLogger(SignInController::class.java)

    val phoneUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()

    @Autowired
    lateinit var authService: AuthService

    @Autowired
    lateinit var jwtService: JWTService

    @RequestMapping(value = ["/email"], method = [RequestMethod.POST])
    @ApiOperation(
            value = "Login with email",
            response  = AccountResponse::class,
    )
    fun authWithEmail(@RequestBody request: SignInRequest?): Response<*> {
        try {
            if (request == null) return Response.badRequest()

            if (request.email.isNullOrEmpty())
                return Response.failed(error = ResultCode.EMAIL_IS_NULL_BLANK)

            if (!request.email.isEmail())
                return Response.failed(error = ResultCode.EMAIL_WRONG_FORMAT)

            if (request.password.isNullOrEmpty())
                return Response.failed(error = ResultCode.PASSWORD_IS_NULL_BLANK)

            if (request.deviceId.isNullOrEmpty() || request.deviceName.isNullOrEmpty())
                return Response.failed(error = ResultCode.UNSUPPORTED_DEVICE)

            log.info(String.format("request with %s %s", request.email, request.password))
            // Find account with same username, check password
            if (!authService.existsByEmail(request.email))
                return Response.failed(error = ResultCode.EMAIL_PASSWORD_INCORRECT)

            // get account by email
            val rawAccount = authService.findByEmail(request.email)
                    ?: return Response.failed(error = ResultCode.EMAIL_PASSWORD_INCORRECT)

            // Validate password
            if (!authService.validatePassword(request.password, rawAccount.password))
                return Response.failed(error = ResultCode.EMAIL_PASSWORD_INCORRECT)

            // Save device
            val device = authService.saveDevice(DeviceEntity(
                    deviceId = request.deviceId,
                    deviceName = request.deviceName,
                    platform = request.platform
            ))

            // Update device after login
            if (rawAccount.devices?.any { element -> element?.deviceId == request.deviceId } == false)
                rawAccount.devices?.add(device)

            val jwt = jwtService.generateJWT(rawAccount.id, request.deviceId)

            val dto = authService.login(rawAccount)
            return Response.success(data = dto, token = jwt)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ResultCode.SERVER_UNKNOWN_ERROR)
        }
    }

    @RequestMapping(value = ["/phone"], method = [RequestMethod.POST])
    @ApiOperation(
            value = "Login with phone number",
            response = AccountResponse::class
    )
    fun authWithPhoneNumber(@RequestBody request: SignInRequest?): Response<*> {
        try {
            if (request == null)
                return Response.failed(error = ResultCode.WARNING_DATA_FORMAT)

            if (request.phoneNumber.isNullOrEmpty())
                return Response.failed(error = ResultCode.EMAIL_IS_NULL_BLANK)

            val numberProto = phoneUtil.parse(request.phoneNumber, request.alpha2Code?.toUpperCase())

            if (!phoneUtil.isValidNumber(numberProto))
                return Response.failed(error = ResultCode.PHONE_NUMBER_WRONG_FORMAT)

            if (request.password.isNullOrEmpty())
                return Response.failed(error = ResultCode.PASSWORD_IS_NULL_BLANK)

            if (request.deviceId.isNullOrEmpty() || request.deviceName.isNullOrEmpty())
                return Response.failed(error = ResultCode.UNSUPPORTED_DEVICE)

            val phoneInterNational = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)

            // Find account with same username, check password
            if (!authService.existsByPhoneNumber(phoneInterNational))
                return Response.failed(error = ResultCode.PHONE_NUMBER_PASSWORD_INCORRECT)

            // get account by phone number
            val rawAccount = authService.findByPhoneNumber(phoneInterNational)
                    ?: return Response.failed(error = ResultCode.PHONE_NUMBER_PASSWORD_INCORRECT)

            // Validate password
            if (!authService.validatePassword(request.password, rawAccount.password))
                return Response.failed(error = ResultCode.PHONE_NUMBER_PASSWORD_INCORRECT)

            // Save device
            val device = authService.saveDevice(DeviceEntity(
                    deviceId = request.deviceId,
                    deviceName = request.deviceName,
                    platform = request.platform
            ))

            // Update device after login
            if (rawAccount.devices?.any { element -> element?.deviceId == request.deviceId } == false)
                rawAccount.devices?.add(device)

            val jwt = jwtService.generateJWT(rawAccount.id, request.deviceId)

            val dto = authService.login(rawAccount)
            return Response.success(data = dto, token = jwt)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ResultCode.SERVER_UNKNOWN_ERROR)
        }
    }

    @RequestMapping(value = ["/facebook"], method = [RequestMethod.POST])
    fun authWithFacebook(@RequestBody json: Map<String, String>): String {
        return "facebook"
    }
}
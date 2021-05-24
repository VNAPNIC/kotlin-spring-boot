package com.vnapnic.auth.controllers

import com.vnapnic.auth.services.AuthService
import com.vnapnic.database.enums.Platform
import com.vnapnic.common.beans.ErrorCode
import com.vnapnic.common.beans.Response
import com.vnapnic.common.service.JWTService
import com.vnapnic.common.utils.isEmail
import com.vnapnic.common.utils.isPhoneNumber
import com.vnapnic.database.beans.DeviceBean
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/login")
class AuthController {
    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @Autowired
    lateinit var authService: AuthService

    @Autowired
    lateinit var jwtService: JWTService

    @RequestMapping(value = ["/email"], method = [RequestMethod.POST])
    fun authWithEmail(@RequestBody json: Map<String, String>): Response {
        try {
            val email: String? = json["email"]
            val password: String? = json["password"]
            // Device
            val deviceName: String? = json["deviceName"]
            val deviceId: String? = json["deviceId"]
            val platform: String? = json["platform"]

            if (email.isNullOrEmpty()) {
                return Response.failed(error = ErrorCode.EMAIL_IS_NULL_BLANK)
            }

            if (!email.isEmail()){
                return Response.failed(error = ErrorCode.EMAIL_WRONG_FORMAT)
            }

            if (password == null || password == "") {
                return Response.failed(error = ErrorCode.PASSWORD_IS_NULL_BLANK)
            }

            if (deviceId == null || deviceName == null || platform == null) {
                return Response.failed(error = ErrorCode.UNSUPPORTED_DEVICE)
            }

            log.info(String.format("request with %s %s", email, password))
            // Find account with same username, check password
            if (!authService.existsByEmail(email))
                return Response.failed(error = ErrorCode.EMAIL_PASSWORD_NOT_CORRECT)

            val rawAccount = authService.findByEmail(email)
                    ?: return Response.failed(error = ErrorCode.EMAIL_PASSWORD_NOT_CORRECT)
            // Validate
            if (!authService.validatePassword(password, rawAccount.password))
                return Response.failed(error = ErrorCode.EMAIL_PASSWORD_NOT_CORRECT)
            val device = DeviceBean(
                    deviceId = deviceId,
                    deviceName = deviceName,
                    platform = Platform.valueOf(platform ?: "")
            )
            // Update device after login
            if (rawAccount.devices?.any { element -> element?.deviceId == deviceId } == false) {
                rawAccount.devices?.add(device)
            }

            val jwt = jwtService.generateJWT(rawAccount._id, deviceId)

            val accountDTO = authService.updateDevices(rawAccount, device)
            return Response.success(data = accountDTO, token = jwt)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ErrorCode.SERVER_UNKNOWN_ERROR)
        }
    }

    @RequestMapping(value = ["/phone"], method = [RequestMethod.POST])
    fun authWithPhoneNumber(@RequestBody json: Map<String, String>): Response {
        val phoneNumber: String? = json["phoneNumber"]
        val password: String? = json["password"]
        // Device
        val deviceName: String? = json["deviceName"]
        val deviceId: String? = json["deviceId"]
        val platform: String? = json["platform"]

        if (phoneNumber.isNullOrEmpty()) {
            return Response.failed(error = ErrorCode.EMAIL_IS_NULL_BLANK)
        }

        if (!phoneNumber.isPhoneNumber()){
            return Response.failed(error = ErrorCode.PHONE_NUMBER_WRONG_FORMAT)
        }

        if (password == null || password == "") {
            return Response.failed(error = ErrorCode.PASSWORD_IS_NULL_BLANK)
        }

        if (deviceId == null || deviceName == null || platform == null) {
            return Response.failed(error = ErrorCode.UNSUPPORTED_DEVICE)
        }
        return Response.unauthorized()
    }

    @RequestMapping(value = ["/facebook"], method = [RequestMethod.POST])
    fun authWithFacebook(@RequestBody json: Map<String, String>): String {
        return "facebook"
    }
}
package com.vnapnic.auth.controllers

import com.vnapnic.auth.services.*
import com.vnapnic.common.exception.SequenceException
import com.vnapnic.database.enums.Role
import com.vnapnic.common.beans.ErrorCode
import com.vnapnic.common.beans.Response
import com.vnapnic.common.utils.isEmail
import com.vnapnic.common.utils.isPhoneNumber
import com.vnapnic.database.beans.AccountBean
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/register")
class RegisterController {
    private val log = LoggerFactory.getLogger(RegisterController::class.java)

    @Autowired
    lateinit var sequenceGeneratorService: SequenceGeneratorService

    @Autowired
    lateinit var authService: AuthService

    /**
     * Staff can use account with the phone number or email
     */
    @RequestMapping(value = ["/staff"], method = [RequestMethod.POST])
    fun registerStaffWithEmail(@RequestBody json: Map<String, String>): Response {
        try {
            val code: String? = json["code"]
            val phoneNumber: String? = json["phoneNumber"]
            val socialId: String? = json["socialId"]
            val email: String? = json["email"]
            val password: String? = json["password"]
            // Device
            val deviceName: String? = json["deviceName"]
            val deviceId: String? = json["deviceId"]
            val platform: String? = json["platform"]

            log.info(String.format("request with %s %s %s %s", phoneNumber, socialId, email, password))

            if (code.isNullOrEmpty()) {
                if (code?.startsWith("S") == false)
                    return Response.failed(error = ErrorCode.CODE_NOT_CORRECT)
            }

            if (email.isNullOrEmpty()) {
                return Response.failed(error = ErrorCode.EMAIL_IS_NULL_BLANK)
            }

            if (!email.isEmail()){
                return Response.failed(error = ErrorCode.EMAIL_WRONG_FORMAT)
            }

            if (phoneNumber.isNullOrEmpty()) {
                return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_NULL_BLANK)
            }

            if (!phoneNumber.isPhoneNumber()){
                return Response.failed(error = ErrorCode.PHONE_NUMBER_WRONG_FORMAT)
            }

            if (password.isNullOrEmpty()) {
                return Response.failed(error = ErrorCode.PASSWORD_IS_NULL_BLANK)
            }

            if (authService.existsByEmail(email)) {
                return Response.failed(error = ErrorCode.EMAIL_IS_EXISTS)
            }

            if (authService.existsByPhoneNumber(phoneNumber)) {
                return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_EXISTS)
            }

            // create staff Id
            val staffId = sequenceIDToStaffId(code ?: "S${Calendar.getInstance().get(Calendar.YEAR)}")

            val accountDTO = authService.save(staffId = staffId,
                    phoneNumber = phoneNumber,
                    socialId = socialId,
                    email = email,
                    password = password,
                    role = Role.STAFF,
                    deviceId = deviceId,
                    deviceName = deviceName,
                    platform = platform)

            return Response.success(data = accountDTO)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ErrorCode.SERVER_UNKNOWN_ERROR)
        }
    }

    private fun sequenceIDToStaffId(code: String): String {
        val sequenceID = sequenceGeneratorService.nextSequenceId(AccountBean.SEQUENCE_NAME)
                ?: throw SequenceException("can't create staffID")
        return when (sequenceID) {
            in 100..999 -> {
                "$code-0${sequenceID}"
            }
            in 10..99 -> {
                "$code-00${sequenceID}"
            }
            in 1..9 -> {
                "$code-000${sequenceID}"
            }
            else -> {
                "$code-$sequenceID"
            }
        }
    }

    /**
     * Customer only account is phone number
     */
    @RequestMapping(value = ["/customer"], method = [RequestMethod.POST])
    fun registerCustomer(@RequestBody json: Map<String, String>): Response {
        try {
            val phoneNumber: String? = json["phoneNumber"]
            val socialId: String? = json["socialId"]
            val email: String? = json["email"]
            val password: String? = json["password"]
            // Device
            val deviceName: String? = json["deviceName"]
            val deviceId: String? = json["deviceId"]
            val platform: String? = json["platform"]

            log.info(String.format("request with %s %s %s %s", phoneNumber, socialId, email, password))

            if (phoneNumber.isNullOrEmpty()) {
                return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_NULL_BLANK)
            }

            if (!phoneNumber.isPhoneNumber()){
                return Response.failed(error = ErrorCode.PHONE_NUMBER_WRONG_FORMAT)
            }

            if (password.isNullOrEmpty()) {
                return Response.failed(error = ErrorCode.PASSWORD_IS_NULL_BLANK)
            }

            if (authService.existsByPhoneNumber(phoneNumber)) {
                return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_EXISTS)
            }

            val accountDTO = authService.save(
                    staffId = null,
                    phoneNumber = phoneNumber,
                    socialId = socialId,
                    email = email,
                    password = password,
                    role = Role.CUSTOMER,
                    deviceId = deviceId,
                    deviceName = deviceName,
                    platform = platform)

            return Response.success(data = accountDTO)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ErrorCode.SERVER_UNKNOWN_ERROR)
        }
    }
}
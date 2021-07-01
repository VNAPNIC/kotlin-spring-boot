package com.vnapnic.auth.controllers

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.vnapnic.auth.dto.*
import com.vnapnic.auth.services.AuthService
import com.vnapnic.auth.services.SequenceGeneratorService
import com.vnapnic.common.dto.AccountResponse
import com.vnapnic.common.entities.Response
import com.vnapnic.common.entities.ResultCode
import com.vnapnic.common.exception.SequenceException
import com.vnapnic.common.utils.isEmail
import com.vnapnic.database.entities.AccountEntity
import com.vnapnic.database.enums.Role
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/register")
class SignUpController {
    private val log = LoggerFactory.getLogger(SignUpController::class.java)

    val phoneUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()

    @Autowired
    lateinit var sequenceGeneratorService: SequenceGeneratorService

    @Autowired
    lateinit var authService: AuthService

    /**
     * Staff can use account with the phone number or email
     */
    @RequestMapping(value = ["/collaborator"], method = [RequestMethod.POST])
    @ApiOperation(
            value = "Login with phone number",
            response = AccountResponse::class
    )
    fun collaboratorRegister(@RequestBody request: SignUpRequest?): Response<*> {
        try {

            if (request == null) return Response.badRequest()

            if (request.code.isNullOrEmpty())
                return Response.failed(error = ResultCode.STAFF_CODE_IS_NULL_BLANK)

            if (request.email.isNullOrEmpty())
                return Response.failed(error = ResultCode.EMAIL_IS_NULL_BLANK)

            if (!request.email.isEmail())
                return Response.failed(error = ResultCode.EMAIL_WRONG_FORMAT)

            if (request.phoneNumber.isNullOrEmpty())
                return Response.failed(error = ResultCode.PHONE_NUMBER_IS_NULL_BLANK)

            val numberProto = phoneUtil.parse(request.phoneNumber, request.alpha2Code?.toUpperCase())

            if (!phoneUtil.isValidNumber(numberProto))
                return Response.failed(error = ResultCode.PHONE_NUMBER_WRONG_FORMAT)

            if (request.password.isNullOrEmpty())
                return Response.failed(error = ResultCode.PASSWORD_IS_NULL_BLANK)

            if (authService.existsByEmail(request.email))
                return Response.failed(error = ResultCode.EMAIL_IS_EXISTS)

            if (authService.existsByPhoneNumber(request.phoneNumber))
                return Response.failed(error = ResultCode.PHONE_NUMBER_IS_EXISTS)

            // create staff Id
            val staffId = sequenceIDToStaffId("${request.code}${Calendar.getInstance().get(Calendar.YEAR)}")
            val phoneInterNational = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)

            val dto = authService.saveAccount(staffId = staffId,
                    phoneNumber = phoneInterNational,
                    socialId = request.socialId,
                    email = request.email,
                    password = request.password,
                    role = Role.STAFF,
                    deviceId = request.deviceId,
                    deviceName = request.deviceName,
                    platform = request.platform)

            return Response.success(data = dto)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ResultCode.SERVER_UNKNOWN_ERROR)
        }
    }

    private fun sequenceIDToStaffId(code: String): String {
        val sequenceID = sequenceGeneratorService.nextSequenceId(AccountEntity.SEQUENCE_NAME)
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
    @ApiOperation(
            value = "Login with phone number",
            response = AccountResponse::class
    )
    fun customerRegister(@RequestBody request: SignUpRequest?): Response<*> {
        try {
            if (request == null) return Response.badRequest()

            if (request.phoneNumber.isNullOrEmpty())
                return Response.failed(error = ResultCode.PHONE_NUMBER_IS_NULL_BLANK)

            val numberProto = phoneUtil.parse(request.phoneNumber, request.alpha2Code?.toUpperCase())

            if (!phoneUtil.isValidNumber(numberProto))
                return Response.failed(error = ResultCode.PHONE_NUMBER_WRONG_FORMAT)

            if (request.password.isNullOrEmpty())
                return Response.failed(error = ResultCode.PASSWORD_IS_NULL_BLANK)

            val phoneInterNational = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)

            if (authService.existsByPhoneNumber(phoneInterNational))
                return Response.failed(error = ResultCode.PHONE_NUMBER_IS_EXISTS)

            val dto = authService.saveAccount(
                    staffId = null,
                    phoneNumber = phoneInterNational,
                    socialId = request.socialId,
                    email = request.email,
                    password = request.password,
                    role = Role.CUSTOMER,
                    deviceId = request.deviceId,
                    deviceName = request.deviceName,
                    platform = request.platform)

            return Response.success(data = dto)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ResultCode.SERVER_UNKNOWN_ERROR)
        }
    }
}
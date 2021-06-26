package com.vnapnic.auth.controllers

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.vnapnic.auth.dto.VerifyCodeRequest
import com.vnapnic.auth.services.AuthService
import com.vnapnic.common.entities.ErrorCode
import com.vnapnic.common.entities.Response
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping
class VerifyController {
    private val log = LoggerFactory.getLogger(RegisterController::class.java)

    val phoneUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()

    @Autowired
    lateinit var authService: AuthService

    @RequestMapping(value = ["/verify"], method = [RequestMethod.GET])
    @ApiOperation(value = "Get verify code")
    fun getVerifyCode(@RequestParam(name = "phoneNumber") phoneNumber: String?,
                      @RequestParam(name = "alpha2Code") alpha2Code: String): Response<*> {
        try {
            if (phoneNumber.isNullOrEmpty())
                return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_NULL_BLANK)

            val numberProto = phoneUtil.parse(phoneNumber, alpha2Code.toUpperCase())

            if (!phoneUtil.isValidNumber(numberProto))
                return Response.failed(error = ErrorCode.PHONE_NUMBER_WRONG_FORMAT)

            val phoneInterNational = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
            val verifyCode = authService.getVerifyCode(phoneInterNational)

            return Response.success(data = verifyCode)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ErrorCode.SERVER_UNKNOWN_ERROR)
        }
    }

    @RequestMapping(value = ["/verify"], method = [RequestMethod.POST])
    @ApiOperation(value = "verify code")
    fun verifyCode(@RequestBody request: VerifyCodeRequest?): Response<*> {
        try {
            if (request == null)
                return Response.failed(error = ErrorCode.WARNING_DATA_FORMAT)

            if (request.phoneNumber.isNullOrEmpty())
                return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_NULL_BLANK)

            val numberProto = phoneUtil.parse(request.phoneNumber, request.alpha2Code?.toUpperCase())

            if (!phoneUtil.isValidNumber(numberProto))
                return Response.failed(error = ErrorCode.PHONE_NUMBER_WRONG_FORMAT)

            if (authService.existsByPhoneNumber(request.phoneNumber))
                return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_EXISTS)

            val phoneInterNational = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
            authService.verifyCode(phoneInterNational)

            return Response.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ErrorCode.SERVER_UNKNOWN_ERROR)
        }
    }
}
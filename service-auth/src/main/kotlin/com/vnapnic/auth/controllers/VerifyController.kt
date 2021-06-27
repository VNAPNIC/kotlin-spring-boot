package com.vnapnic.auth.controllers

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.vnapnic.auth.dto.SendVerifyCodeRequest
import com.vnapnic.auth.dto.VerifyCodeRequest
import com.vnapnic.auth.services.AuthService
import com.vnapnic.common.entities.ResultCode
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

    @RequestMapping(value = ["/sendVerifyCode"], method = [RequestMethod.POST])
    @ApiOperation(value = "Send verify code")
    fun sendVerifyCode(@RequestBody request: SendVerifyCodeRequest?): Response<*> {
        try {
            if (request == null)
                return Response.failed(error = ResultCode.WARNING_DATA_FORMAT)

            if (request.phoneNumber.isNullOrEmpty())
                return Response.failed(error = ResultCode.PHONE_NUMBER_IS_NULL_BLANK)

            val numberProto = phoneUtil.parse(request.phoneNumber, request.alpha2Code?.toUpperCase())

            if (!phoneUtil.isValidNumber(numberProto))
                return Response.failed(error = ResultCode.PHONE_NUMBER_WRONG_FORMAT)

            if (authService.existsByPhoneNumber(request.phoneNumber))
                return Response.failed(error = ResultCode.PHONE_NUMBER_IS_EXISTS)

            val phoneInterNational = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
            val verifyCode = authService.sendVerifyCode(phoneInterNational)

            return Response.success(data = verifyCode)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ResultCode.SERVER_UNKNOWN_ERROR)
        }
    }

    @RequestMapping(value = ["/verify"], method = [RequestMethod.POST])
    @ApiOperation(value = "verify code")
    fun verifyCode(@RequestBody request: VerifyCodeRequest?): Response<*> {
        try {
            if (request == null)
                return Response.failed(error = ResultCode.WARNING_DATA_FORMAT)

            if (request.verifyCode == null || request.verifyCode < 1000 || request.verifyCode > 9999)
                return Response.failed(error = ResultCode.VERIFY_CODE_NOT_CORRECT)

            if (request.phoneNumber.isNullOrEmpty())
                return Response.failed(error = ResultCode.PHONE_NUMBER_IS_NULL_BLANK)

            val numberProto = phoneUtil.parse(request.phoneNumber, request.alpha2Code?.toUpperCase())

            if (!phoneUtil.isValidNumber(numberProto))
                return Response.failed(error = ResultCode.PHONE_NUMBER_WRONG_FORMAT)

            if (authService.existsByPhoneNumber(request.phoneNumber))
                return Response.failed(error = ResultCode.PHONE_NUMBER_IS_EXISTS)

            val phoneInterNational = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
            val resultCode = authService.verifyCode(phoneInterNational, request.verifyCode)

            return if (resultCode == ResultCode.SUCCESS) {
                Response.success()
            } else {
                Response.failed(error = resultCode)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ResultCode.SERVER_UNKNOWN_ERROR)
        }
    }

    @RequestMapping(value = ["/verify"], method = [RequestMethod.GET])
    @ApiOperation(value = "Get verify code")
    fun getVerifyCode(@RequestParam(name = "phoneNumber") phoneNumber: String?,
                      @RequestParam(name = "alpha2Code") alpha2Code: String): Response<*> {
        try {
            if (phoneNumber.isNullOrEmpty())
                return Response.failed(error = ResultCode.PHONE_NUMBER_IS_NULL_BLANK)

            val numberProto = phoneUtil.parse(phoneNumber, alpha2Code.toUpperCase())

            if (!phoneUtil.isValidNumber(numberProto))
                return Response.failed(error = ResultCode.PHONE_NUMBER_WRONG_FORMAT)

            val phoneInterNational = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
            val verifyCode = authService.getVerifyCode(phoneInterNational)

            return Response.success(data = verifyCode)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ResultCode.SERVER_UNKNOWN_ERROR)
        }
    }
}
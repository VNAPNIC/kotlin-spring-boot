package com.vnapnic.auth.controllers

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.vnapnic.auth.dto.GetVerifyCodeRequest
import com.vnapnic.auth.dto.VerifyCodeRequest
import com.vnapnic.auth.dto.VerifyType
import com.vnapnic.auth.exception.VerifyCodeExpireException
import com.vnapnic.auth.exception.VerifyCodeIncorrectException
import com.vnapnic.auth.exception.WrongTooManyTimesException
import com.vnapnic.auth.services.AuthService
import com.vnapnic.common.entities.Response
import com.vnapnic.common.entities.ResultCode
import com.vnapnic.common.service.JWTService
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

    @Autowired
    lateinit var jwtService: JWTService

    @RequestMapping(value = ["/getVerifyCode"], method = [RequestMethod.POST])
    @ApiOperation(value = "Get verify code")
    fun getVerifyCode(@RequestBody request: GetVerifyCodeRequest?): Response<*> {
        try {
            if (request == null) return Response.badRequest()

            if (request.phoneNumber.isNullOrEmpty())
                return Response.failed(error = ResultCode.PHONE_NUMBER_IS_NULL_BLANK)

            val numberProto = phoneUtil.parse(request.phoneNumber, request.alpha2Code?.toUpperCase())

            if (!phoneUtil.isValidNumber(numberProto))
                return Response.failed(error = ResultCode.PHONE_NUMBER_WRONG_FORMAT)

            val phoneInterNational = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
            val rawAccount = authService.findByPhoneNumber(phoneInterNational)
            return if (rawAccount == null || !rawAccount.phoneVerified) {
                authService.sendVerifyCode(phoneInterNational, request.type)
                Response.success(data = phoneInterNational)
            } else {
                Response.failed(error = ResultCode.PHONE_NUMBER_IS_EXISTS)
            }
        } catch (e: NumberParseException) {
            return Response.failed(error = ResultCode.PHONE_NUMBER_WRONG_FORMAT)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ResultCode.SERVER_UNKNOWN_ERROR)
        }
    }

    @RequestMapping(value = ["/verify"], method = [RequestMethod.POST])
    @ApiOperation(value = "verify code")
    fun verifyCode(@RequestBody request: VerifyCodeRequest?): Response<*> {
        try {
            if (request == null) return Response.badRequest()

            if (request.phoneNumber.isNullOrEmpty())
                return Response.failed(error = ResultCode.PHONE_NUMBER_IS_NULL_BLANK)

            val numberProto = phoneUtil.parse(request.phoneNumber, request.alpha2Code?.toUpperCase())

            if (!phoneUtil.isValidNumber(numberProto))
                return Response.failed(error = ResultCode.PHONE_NUMBER_WRONG_FORMAT)

            val phoneInterNational = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
            val rawAccount = authService.findByPhoneNumber(phoneInterNational)

            return if (rawAccount == null || !rawAccount.phoneVerified) {
                val account = authService.verifyCode(
                        phoneInterNational,
                        request.verifyCode,
                        request.deviceId,
                        request.deviceName,
                        request.platform,
                        request.type,
                        request.role
                )
                        ?: return Response.failed(error = ResultCode.SERVER_UNKNOWN_ERROR)

                log.info(account.toString())

                val jwt = jwtService.generateJWT(account.id, request.deviceId)

                Response.success(data = account, token = jwt)
            } else {
                Response.failed(error = ResultCode.PHONE_NUMBER_IS_EXISTS)
            }
        } catch (e: VerifyCodeIncorrectException) {
            return Response.failedWithData(error = ResultCode.VERIFY_CODE_INCORRECT, errorBody = e.incorrectCount)
        } catch (e: VerifyCodeExpireException) {
            return Response.failed(error = ResultCode.VERIFY_CODE_EXPIRE)
        } catch (e: WrongTooManyTimesException) {
            return Response.failed(error = ResultCode.WRONG_TOO_MANY_TIME)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ResultCode.SERVER_UNKNOWN_ERROR)
        }
    }

    @RequestMapping(value = ["/verify"], method = [RequestMethod.GET])
    @ApiOperation(value = "Get verify code")
    fun getVerifyCode(@RequestParam(name = "phoneNumber") phoneNumber: String?,
                      @RequestParam(name = "alpha2Code") alpha2Code: String,
                      @RequestParam(name = "type") type: VerifyType): Response<*> {
        try {
            if (phoneNumber.isNullOrEmpty())
                return Response.failed(error = ResultCode.PHONE_NUMBER_IS_NULL_BLANK)

            val numberProto = phoneUtil.parse(phoneNumber, alpha2Code.toUpperCase())

            if (!phoneUtil.isValidNumber(numberProto))
                return Response.failed(error = ResultCode.PHONE_NUMBER_WRONG_FORMAT)

            val phoneInterNational = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
            val verifyCode = authService.getVerifyCode(phoneInterNational, type)

            return Response.success(data = verifyCode)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ResultCode.SERVER_UNKNOWN_ERROR)
        }
    }
}
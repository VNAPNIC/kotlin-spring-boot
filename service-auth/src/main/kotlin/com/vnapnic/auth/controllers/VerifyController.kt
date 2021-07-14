package com.vnapnic.auth.controllers

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.vnapnic.auth.dto.*
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
@RequestMapping("/verify")
class VerifyController {
    private val log = LoggerFactory.getLogger(SignUpController::class.java)

    val phoneUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()

    @Autowired
    lateinit var authService: AuthService

    @Autowired
    lateinit var jwtService: JWTService

    @RequestMapping(value = ["/send"], method = [RequestMethod.POST])
    @ApiOperation(value = "Request to send verification code")
    fun sendVerifyCode(@RequestParam(name = "type") type: SendVerifyType, @RequestBody requestSendToVerifyCode: RequestSendToVerifyCode?): Response<*> {
        try {
            if (requestSendToVerifyCode == null) return Response.badRequest()

            if (requestSendToVerifyCode.phoneNumber.isNullOrEmpty())
                return Response.failed(error = ResultCode.PHONE_NUMBER_IS_NULL_BLANK)

            val numberProto = phoneUtil.parse(requestSendToVerifyCode.phoneNumber, requestSendToVerifyCode.alpha2Code?.toUpperCase())

            if (!phoneUtil.isValidNumber(numberProto))
                return Response.failed(error = ResultCode.PHONE_NUMBER_WRONG_FORMAT)

            val phoneInterNational = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)


            val account = authService.findByPhoneNumber(phoneInterNational)

            log.info("type -------------------------> $type")

            if (type == SendVerifyType.LOGIN && account == null) {
                return Response.failed(error = ResultCode.PHONE_NUMBER_NOT_EXISTS)
            }

            if (type == SendVerifyType.REGISTER && account?.phoneVerified == true) {
                return Response.failed(error = ResultCode.PHONE_NUMBER_IS_EXISTS)
            }

            authService.sendVerifyCode(phoneInterNational, requestSendToVerifyCode.typeCode)
            return Response.success(data = phoneInterNational)

        } catch (e: NumberParseException) {
            return Response.failed(error = ResultCode.PHONE_NUMBER_WRONG_FORMAT)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ResultCode.SERVER_UNKNOWN_ERROR)
        }
    }

    @RequestMapping(value = ["/register"], method = [RequestMethod.POST])
    @ApiOperation(value = "Check registration verification code")
    fun registrationVerification(@RequestBody request: VerifyCodeRequest?): Response<*> {
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
                val account = authService.registrationVerification(
                        phoneInterNational,
                        request.verifyCode,
                        request.deviceId,
                        request.deviceName,
                        request.platform,
                        request.typeCode,
                        request.role
                ) ?: return Response.failed(error = ResultCode.SERVER_UNKNOWN_ERROR)

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

    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    @ApiOperation(value = "Check login verification code")
    fun loginVerification(@RequestBody request: VerifyCodeRequest?): Response<*> {
        try {
            if (request == null) return Response.badRequest()

            if (request.phoneNumber.isNullOrEmpty())
                return Response.failed(error = ResultCode.PHONE_NUMBER_IS_NULL_BLANK)

            val numberProto = phoneUtil.parse(request.phoneNumber, request.alpha2Code?.toUpperCase())

            if (!phoneUtil.isValidNumber(numberProto))
                return Response.failed(error = ResultCode.PHONE_NUMBER_WRONG_FORMAT)

            val phoneInterNational = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
            val rawAccount = authService.findByPhoneNumber(phoneInterNational)

            return if (rawAccount?.phoneVerified == true) {
                val account = authService.loginVerification(
                        phoneInterNational,
                        request.verifyCode,
                        request.deviceId,
                        request.deviceName,
                        request.platform,
                        request.typeCode,
                        request.role
                ) ?: return Response.failed(error = ResultCode.PHONE_NUMBER_NOT_EXISTS)

                val jwt = jwtService.generateJWT(account.id, request.deviceId)
                Response.success(data = account, token = jwt)
            } else {
                Response.failed(error = ResultCode.PHONE_NUMBER_NOT_EXISTS)
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

    @RequestMapping(value = ["/code"], method = [RequestMethod.GET])
    @ApiOperation(value = "Get verify code")
    fun sendVerifyCode(@RequestParam(name = "phoneNumber") phoneNumber: String?,
                       @RequestParam(name = "alpha2Code") alpha2Code: String,
                       @RequestParam(name = "typeCode") typeCode: VerifyTypeCode): Response<*> {
        try {
            if (phoneNumber.isNullOrEmpty())
                return Response.failed(error = ResultCode.PHONE_NUMBER_IS_NULL_BLANK)

            val numberProto = phoneUtil.parse(phoneNumber, alpha2Code.toUpperCase())

            if (!phoneUtil.isValidNumber(numberProto))
                return Response.failed(error = ResultCode.PHONE_NUMBER_WRONG_FORMAT)

            val phoneInterNational = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
            val verifyCode = authService.getVerifyCode(phoneInterNational, typeCode)

            return Response.success(data = verifyCode)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ResultCode.SERVER_UNKNOWN_ERROR)
        }
    }
}
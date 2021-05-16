package com.vnapnic.auth.controllers

import com.vnapnic.auth.services.AuthService
import com.vnapnic.auth.services.SequenceGeneratorService
import com.vnapnic.common.db.Account
import com.vnapnic.common.dto.AccountDTO
import com.vnapnic.common.models.ErrorCode
import com.vnapnic.common.models.Response
import io.jsonwebtoken.lang.Assert
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
    lateinit var service: AuthService

    @Autowired
    lateinit var sequenceService: SequenceGeneratorService

    @RequestMapping(value = ["/staff"], method = [RequestMethod.POST])
    fun registerStaffWithEmail(@RequestBody json: Map<String, String>): Response {
        val code: String? = json["code"]
        val phoneNumber: String? = json["phoneNumber"]
        val socialId: String? = json["socialId"]
        val email: String? = json["email"]
        val password: String? = json["password"]
        val cccdFront: String? = json["cccdFront"]
        val cccdBack: String? = json["cccdBack"]

        log.info(String.format(
                "request with %s %s %s %s %s %s", email,
                phoneNumber, socialId, email, password, cccdFront, cccdBack)
        )

        if (code != null || code != "") {
            if (code?.startsWith("S") == false)
                return Response.failed(error = ErrorCode.CODE_NOT_CORRECT)
        }

        if (email == null || email == "") {
            return Response.failed(error = ErrorCode.EMAIL_IS_NULL_BLANK)
        }

        if (phoneNumber == null || phoneNumber == "") {
            return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_NULL_BLANK)
        }

        if (password == null || password == "") {
            return Response.failed(error = ErrorCode.PASSWORD_IS_NULL_BLANK)
        }

        if (service.existsByEmail(email)) {
            return Response.failed(error = ErrorCode.EMAIL_IS_EXISTS)
        }

        if (service.existsByPhoneNumber(phoneNumber)) {
            return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_EXISTS)
        }

        service.save(Account(
                phoneNumber = phoneNumber,
                socialId = socialId,
                email = email,
                password = service.encryptPassword(password),
                cccdFront = cccdFront,
                cccdBack = cccdBack,
                staffId = sequenceIDToStaffId(code ?: "S${Calendar.getInstance().get(Calendar.YEAR)}")
        ))

        val account = service.byEmail(email)
        val accountDTO = AccountDTO(
                id = account?.id,
                socialId = account?.socialId,
                email = account?.email,
                active = account?.active,
                verified = account?.verified,
                staffId = account?.staffId
        )

        return Response.success(data = accountDTO)
    }

    private fun sequenceIDToStaffId(code: String): String = when (
        val sequenceID = sequenceService.nextSequenceId(Account.SEQUENCE_NAME) ?: 1
        ) {
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

    @RequestMapping(value = ["/customer/email"], method = [RequestMethod.POST])
    fun registerCustomer(@RequestBody json: Map<String, String>): Response {
        Assert.isTrue(!json.isNullOrEmpty(), "Missing json.")
        Assert.isTrue(json.containsKey("email"), "Missing email.")
        Assert.isTrue(json.containsKey("password"), "Missing password.")

        val phoneNumber: String? = json["phoneNumber"]
        val socialId: String? = json["socialId"]
        val email: String? = json["email"]
        val password: String? = json["password"]
        val cccdFront: String? = json["cccdFront"]
        val cccdBack: String? = json["cccdBack"]

        log.info(String.format(
                "request with %s %s %s %s %s %s", email,
                phoneNumber, socialId, email, password, cccdFront, cccdBack)
        )

        if (phoneNumber == null || phoneNumber == "") {
            return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_NULL_BLANK)
        }

        if (password == null || password == "") {
            return Response.failed(error = ErrorCode.PASSWORD_IS_NULL_BLANK)
        }

        if (service.existsByPhoneNumber(phoneNumber)) {
            return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_EXISTS)
        }

        service.save(Account(
                phoneNumber = phoneNumber,
                socialId = socialId,
                email = email,
                password = service.encryptPassword(password),
                cccdFront = cccdFront,
                cccdBack = cccdBack
        ))

        val account = service.byEmail(email)
        val accountDTO = AccountDTO(
                id = account?.id,
                socialId = account?.socialId,
                email = account?.email,
                active = account?.active,
                verified = account?.verified
        )

        return Response.success(data = accountDTO)
    }
}
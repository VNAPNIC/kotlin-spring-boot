package com.vnapnic.auth.controllers

import com.vnapnic.common.exception.SequenceException
import com.vnapnic.auth.services.AuthService
import com.vnapnic.auth.services.StaffSequenceGeneratorService
import com.vnapnic.auth.services.UserSequenceGeneratorService
import com.vnapnic.common.db.Account
import com.vnapnic.common.db.Role
import com.vnapnic.common.db.User
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
    lateinit var staffSequenceService: StaffSequenceGeneratorService

    @Autowired
    lateinit var userSequenceService: UserSequenceGeneratorService

    @RequestMapping(value = ["/staff"], method = [RequestMethod.POST])
    fun registerStaffWithEmail(@RequestBody json: Map<String, String>): Response {
        val code: String? = json["code"]
        val phoneNumber: String? = json["phoneNumber"]
        val socialId: String? = json["socialId"]
        val email: String? = json["email"]
        val password: String? = json["password"]

        log.info(String.format("request with %s %s %s %s", phoneNumber, socialId, email, password))

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
        try {
            val staffId = sequenceIDToStaffId(code ?: "S${Calendar.getInstance().get(Calendar.YEAR)}")
            val userId = userSequenceService.nextSequenceId(User.SEQUENCE_NAME).toString()

            service.saveAccount(Account(
                    phoneNumber = phoneNumber,
                    socialId = socialId,
                    email = email,
                    password = service.encryptPassword(password),
                    staffId = sequenceIDToStaffId(staffId),
                    role = Role.STAFF,
                    info = User(id = userId)
            ))

            val account = service.byEmail(email)
            val accountDTO = AccountDTO(
                    id = account?._id,
                    socialId = account?.socialId,
                    email = account?.email,
                    active = account?.active,
                    verified = account?.verified,
                    staffId = account?.staffId,
                    role = account?.role,
                    user = account?.info
            )

            return Response.success(data = accountDTO)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ErrorCode.SERVER_UNKNOWN_ERROR)
        }
    }

    private fun sequenceIDToStaffId(code: String): String {
        val sequenceID = staffSequenceService.nextSequenceId(Account.SEQUENCE_NAME)
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

    @RequestMapping(value = ["/customer"], method = [RequestMethod.POST])
    fun registerCustomer(@RequestBody json: Map<String, String>): Response {
        Assert.isTrue(!json.isNullOrEmpty(), "Missing json.")
        Assert.isTrue(json.containsKey("email"), "Missing email.")
        Assert.isTrue(json.containsKey("password"), "Missing password.")

        val phoneNumber: String? = json["phoneNumber"]
        val socialId: String? = json["socialId"]
        val email: String? = json["email"]
        val password: String? = json["password"]

        log.info(String.format("request with %s %s %s %s", phoneNumber, socialId, email, password))

        if (phoneNumber == null || phoneNumber == "") {
            return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_NULL_BLANK)
        }

        if (password == null || password == "") {
            return Response.failed(error = ErrorCode.PASSWORD_IS_NULL_BLANK)
        }

        if (service.existsByPhoneNumber(phoneNumber)) {
            return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_EXISTS)
        }

        service.saveAccount(Account(
                phoneNumber = phoneNumber,
                socialId = socialId,
                email = email,
                password = service.encryptPassword(password),
                role = Role.CUSTOMER
        ))

        val account = service.byEmail(email)
        val accountDTO = AccountDTO(
                id = account?._id,
                socialId = account?.socialId,
                email = account?.email,
                active = account?.active,
                verified = account?.verified,
                role = account?.role
        )

        return Response.success(data = accountDTO)
    }
}
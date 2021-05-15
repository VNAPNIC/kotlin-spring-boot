package com.vnapnic.auth.controllers

import com.vnapnic.auth.services.AuthService
import com.vnapnic.common.dto.AccountDTO
import com.vnapnic.common.models.ErrorCode
import com.vnapnic.common.models.Response
import com.vnapnic.common.service.JWTService
import io.jsonwebtoken.lang.Assert
import org.apache.http.auth.AuthenticationException
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
    lateinit var service: AuthService

    @Autowired
    lateinit var jwtService: JWTService

    @RequestMapping(value = ["/email"], method = [RequestMethod.POST])
    fun authWithEmail(@RequestBody json: Map<String, String>): Response<AccountDTO> {

        val email: String? = json["email"]
        val password: String? = json["password"]
        var accountDTO: AccountDTO?
        var jwt: String?

        if (email == null || email == "") {
            return Response.failed(error = ErrorCode.EMAIL_IS_NULL_BLANK)
        }

        if (password == null || password == "") {
            return Response.failed(error = ErrorCode.PASSWORD_IS_NULL_BLANK)
        }

        log.info(String.format("request with %s %s", email, password))
        // Find account with same username, check password
        if (service.existsByEmail(email)) {
            val account = service.byEmail(email)

            accountDTO = AccountDTO(
                    id = account?.id,
                    socialId = account?.socialId,
                    email = account?.email,
                    active = account?.active,
                    verified = account?.verified
            )

            // Validate
            if (service.validatePassword(password, account?.password)) {
                jwt = jwtService.generateJWT(accountDTO)
            } else {
                return Response.failed(error = ErrorCode.EMAIL_PASSWORD_NOT_CORRECT)
            }
        } else {
            return Response.failed(error = ErrorCode.EMAIL_PASSWORD_NOT_CORRECT)
        }
        return Response.success(data = accountDTO, token = jwt)
    }

    @RequestMapping(value = ["/facebook"], method = [RequestMethod.POST])
    fun authWithFacebook(@RequestBody json: Map<String, String>): String {
        return "facebook"
    }
}
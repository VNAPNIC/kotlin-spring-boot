package com.vnapnic.auth.controllers

import com.vnapnic.auth.services.AuthService
import com.vnapnic.common.models.Account
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
@RequestMapping("/auth")
class AuthController {
    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @Autowired
    lateinit var service: AuthService

    @Autowired
    lateinit var jwtService: JWTService

    @RequestMapping(value = ["/email"], method = [RequestMethod.POST])
    fun authWithEmail(@RequestBody json: Map<String, String>): Response<Account> {
        Assert.isTrue(!json.isNullOrEmpty(), "Missing json.")
        Assert.isTrue(json.containsKey("email"), "Missing email.")
        Assert.isTrue(json.containsKey("password"), "Missing password.")

        val email = json["email"].toString()
        var account: Account? = null
        var jwt: String? = null

        log.info(String.format("request with %s %s", email, json["password"]))
        // Find account with same username, check password
        if (service.byEmail(email) != null) {
            account = service.byEmail(email)
            // Validate
            if (service.validatePassword(json["password"].toString(), account)) {
                jwt = jwtService.generateJWT(account)
            } else {
                throw AuthenticationException("Username/Password is not correct.");
            }
        }
        return Response.success(data = account, token = jwt)
    }

    @RequestMapping(value = ["/facebook"], method = [RequestMethod.POST])
    fun authWithFacebook(@RequestBody json: Map<String, String>): String {
        return "facebook"
    }
}
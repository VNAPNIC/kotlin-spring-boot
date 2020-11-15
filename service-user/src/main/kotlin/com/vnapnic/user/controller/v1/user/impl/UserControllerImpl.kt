package com.vnapnic.user.controller.v1.user.impl

import com.vnapnic.user.controller.v1.user.UserController
import com.vnapnic.user.model.exception.BaseException
import com.vnapnic.user.model.exception.ExceptionEnum
import com.vnapnic.user.model.user.request.UserRegisterReq
import com.vnapnic.user.model.user.response.UserRes
import com.vnapnic.user.service.user.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/user")
class UserControllerImpl(private val service: UserService) : UserController {

    @PostMapping("/user")
    @PreAuthorize("#oauth2.hasScope('Visitor-Restricted')")
    override fun registerUser(@RequestBody request: UserRegisterReq): UserRes {
        if (!service.checkNewPasswordValid(request.password))
            throw BaseException(ExceptionEnum.PASSWORD_INVALID)
        if (request.username != null && !service.checkNewUsernameValid(request.username))
            throw BaseException(ExceptionEnum.USERNAME_INVALID)
//        if (!service.checkVerificationCode(request.email, request.verifyCode))
//            throw BaseException(ExceptionEnum.SAFE_VERIFY_CODE_ERROR)
        return UserRes(service.saveUser(request.toUser()))
    }

}
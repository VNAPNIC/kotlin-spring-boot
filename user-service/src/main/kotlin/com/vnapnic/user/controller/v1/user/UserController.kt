package com.vnapnic.user.controller.v1.user

import com.vnapnic.user.model.user.request.UserRegisterReq
import com.vnapnic.user.model.user.response.UserRes

interface UserController {
    fun registerUser(request: UserRegisterReq): UserRes
}
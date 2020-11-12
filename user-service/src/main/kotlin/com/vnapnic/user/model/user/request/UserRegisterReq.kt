package com.vnapnic.user.model.user.request

import com.vnapnic.common.AESOperation
import com.vnapnic.user.model.user.entity.Role
import com.vnapnic.user.model.user.entity.User
import java.time.ZonedDateTime
import java.util.*

data class UserRegisterReq(val username: String?,
                           val password: String,
                           val email: String,
                           val verifyCode: String) : java.io.Serializable {
    fun toUser(): User {
        return User(
                null,
                System.currentTimeMillis().toString() + "-" + UUID.randomUUID().toString(),
                username
                        ?: email.substringBeforeLast("@") + UUID.randomUUID().toString().substring(24..33),
                AESOperation.instance.encrypt(password),
                email,
                null,
                null,
                ZonedDateTime.now(),
                true,
                Role(2, "ROLE_USER")
        )
    }
}
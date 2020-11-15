package com.vnapnic.user.model.user.response

import com.vnapnic.user.model.user.entity.User

data class UserRes(val userId: String,
                    val username: String,
                    var email: String?,
                    var phone: String?,
                    val avatar: String?,
                    val version: Int) : java.io.Serializable {
    constructor(user: User) : this(user.userId, user.username, user.email, user.phone, user.avatar, user.version)
}
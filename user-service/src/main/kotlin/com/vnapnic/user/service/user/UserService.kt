package com.vnapnic.user.service.user

import com.vnapnic.user.model.user.entity.User

interface UserService {

    fun saveUser(user: User): User

    fun checkNewPasswordValid(password: String): Boolean

    fun checkNewUsernameValid(username: String): Boolean
}
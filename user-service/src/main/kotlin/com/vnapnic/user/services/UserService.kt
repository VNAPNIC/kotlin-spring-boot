package com.vnapnic.user.services

import org.springframework.stereotype.Service

interface UserService {
    fun updateAvatar(accountId: String?, avatarId: String?)
}

@Service
class UserServiceImpl() : UserService {

    override fun updateAvatar(accountId: String?, avatarId: String?) {
    }
}
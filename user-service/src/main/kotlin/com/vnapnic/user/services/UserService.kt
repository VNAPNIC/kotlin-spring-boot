package com.vnapnic.user.services

interface UserService {
    fun updateAvatar(accountId: String?, avatarId: String?)
}

class UserServiceImpl() : UserService {

    override fun updateAvatar(accountId: String?, avatarId: String?) {
    }

}
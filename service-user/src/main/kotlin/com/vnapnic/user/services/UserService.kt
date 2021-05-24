package com.vnapnic.user.services

import com.vnapnic.database.enums.Gender
import com.vnapnic.user.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface UserService {

    fun findById(userId: String)

    fun updateProfile(userId: String, firstName: String?, lastName: String?, weight: Double?, height: Double?, gender: Gender, description: String?)
}

@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    override fun findById(userId: String) {
    }

    override fun updateProfile(userId: String, firstName: String?, lastName: String?, weight: Double?, height: Double?, gender: Gender, description: String?) {

    }
}
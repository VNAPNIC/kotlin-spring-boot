package com.vnapnic.auth.services

import com.vnapnic.auth.model.Account
import com.vnapnic.auth.property.ApplicationProperty
import com.vnapnic.auth.repositories.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


internal interface AuthService {
    fun bySocialId(socialId: String?): Account?
    fun byEmail(email: String?): Account?
    fun save(account: Account?): Account?
    fun validatePassword(password: String?, account: Account?): Boolean
    fun encryptPassword(password: String?): String?
    fun generateJWT(account: Account?): String?
    fun parseJWT(token: String?): Account?
}

@Service
class AuthServiceImpl: AuthService {

    @Autowired lateinit var accountRepository: AccountRepository
    @Autowired lateinit var  passwordEncoder: PasswordEncoder
    @Autowired lateinit var  property: ApplicationProperty

    override fun bySocialId(socialId: String?): Account? {
    }

    override fun byEmail(email: String?): Account? {
    }

    override fun save(account: Account?): Account? {
    }

    override fun validatePassword(password: String?, account: Account?): Boolean {
    }

    override fun encryptPassword(password: String?): String? {
    }

    override fun generateJWT(account: Account?): String? {
    }

    override fun parseJWT(token: String?): Account? {
    }

}
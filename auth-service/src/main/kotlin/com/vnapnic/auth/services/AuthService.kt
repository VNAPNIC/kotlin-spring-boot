package com.vnapnic.auth.services

import com.vnapnic.auth.model.Account
import com.vnapnic.auth.repositories.AccountRepository
import com.vnapnic.common.service.JWTServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


interface AuthService {
    fun bySocialId(socialId: String?): Account?
    fun byEmail(email: String?): Account?

    fun existsBySocialId(socialId: String?): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean

    fun save(account: Account?): Account?
    fun validatePassword(rawPassword: String?, encodedPassword: String?): Boolean
    fun encryptPassword(password: String?): String?
}

@Service
class AuthServiceImpl : JWTServiceImpl(), AuthService {

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    override fun bySocialId(socialId: String?): Account? = accountRepository.findBySocialId(socialId)

    override fun byEmail(email: String?): Account? = accountRepository.findByEmail(email)

    override fun existsBySocialId(socialId: String?): Boolean = accountRepository.existsBySocialId(socialId)
    override fun existsByEmail(email: String): Boolean = accountRepository.existsByEmail(email)
    override fun existsByPhoneNumber(phoneNumber: String): Boolean = accountRepository.existsByPhoneNumber(phoneNumber)

    override fun save(account: Account?): Account? = if (account != null) accountRepository.save(account) else null

    override fun validatePassword(rawPassword: String?, encodedPassword: String?): Boolean = passwordEncoder.matches(rawPassword, encodedPassword)
    override fun encryptPassword(password: String?): String? = passwordEncoder.encode(password)
}
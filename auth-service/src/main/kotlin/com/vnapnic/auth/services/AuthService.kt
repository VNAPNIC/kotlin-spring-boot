package com.vnapnic.auth.services

import com.vnapnic.auth.repositories.AccountRepository
import com.vnapnic.auth.repositories.UserRepository
import com.vnapnic.common.db.Account
import com.vnapnic.common.db.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface AuthService {
    fun bySocialId(socialId: String?): Account?
    fun byEmail(email: String?): Account?

    fun existsBySocialId(socialId: String?): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean

    fun saveAccount(account: Account?): Account?
    fun saveUserInfo(user: User?): User?
    fun validatePassword(rawPassword: String?, encodedPassword: String?): Boolean
    fun encryptPassword(password: String?): String?
}

@Service
class AuthServiceImpl :  AuthService {

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    override fun bySocialId(socialId: String?): Account? = accountRepository.findBySocialId(socialId)

    override fun byEmail(email: String?): Account? = accountRepository.findByEmail(email)

    override fun existsBySocialId(socialId: String?): Boolean = accountRepository.existsBySocialId(socialId)
    override fun existsByEmail(email: String): Boolean = accountRepository.existsByEmail(email)
    override fun existsByPhoneNumber(phoneNumber: String): Boolean = accountRepository.existsByPhoneNumber(phoneNumber)

    override fun saveAccount(account: Account?): Account? = if (account != null) accountRepository.save(account) else null
    override fun saveUserInfo(user: User?): User? = if (user !=null) userRepository.save(user) else null

    override fun validatePassword(rawPassword: String?, encodedPassword: String?): Boolean = passwordEncoder.matches(rawPassword, encodedPassword)
    override fun encryptPassword(password: String?): String? = passwordEncoder.encode(password)
}
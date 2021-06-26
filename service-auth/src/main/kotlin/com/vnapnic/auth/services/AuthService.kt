package com.vnapnic.auth.services

import com.vnapnic.auth.controllers.RegisterController
import com.vnapnic.auth.repositories.AccountRepository
import com.vnapnic.auth.repositories.DeviceRepository
import com.vnapnic.auth.repositories.LoginHistoryRepository
import com.vnapnic.auth.repositories.UserRepository
import com.vnapnic.auth.dto.AccountResponse
import com.vnapnic.common.service.RedisService
import com.vnapnic.database.entities.AccountEntity
import com.vnapnic.database.entities.DeviceEntity
import com.vnapnic.database.entities.LoginHistoryEntity
import com.vnapnic.database.entities.UserEntity
import com.vnapnic.database.enums.Platform
import com.vnapnic.database.enums.Role
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

interface AuthService {

    fun findBySocialId(socialId: String?): AccountEntity?
    fun findByPhoneNumber(phoneNumber: String?): AccountEntity?
    fun findByEmail(email: String?): AccountEntity?

    fun existsBySocialId(socialId: String?): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean

    fun verifyCode(phoneNumber: String)

    fun getVerifyCode(phoneNumber: String): Int?

    fun saveAccount(staffId: String?,
                    phoneNumber: String?,
                    socialId: String?,
                    email: String?,
                    password: String?,
                    role: Role?,
                    deviceId: String?,
                    deviceName: String?,
                    platform: String?): AccountResponse?

    fun saveDevice(device: DeviceEntity): DeviceEntity?

    fun login(account: AccountEntity): AccountResponse?

    fun validatePassword(rawPassword: String?, encodedPassword: String?): Boolean
    fun encryptPassword(password: String?): String?
}

@Service
class AuthServiceImpl : AuthService {
    private val log = LoggerFactory.getLogger(RegisterController::class.java)

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var deviceRepository: DeviceRepository

    @Autowired
    lateinit var loginHistoryRepository: LoginHistoryRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var redisService: RedisService

    override fun findBySocialId(socialId: String?): AccountEntity? = accountRepository.findBySocialId(socialId)
    override fun findByPhoneNumber(phoneNumber: String?): AccountEntity? = accountRepository.findByPhoneNumber(phoneNumber)
    override fun findByEmail(email: String?): AccountEntity? = accountRepository.findByEmail(email)

    override fun existsBySocialId(socialId: String?): Boolean = accountRepository.existsBySocialId(socialId)
    override fun existsByEmail(email: String): Boolean = accountRepository.existsByEmail(email)
    override fun existsByPhoneNumber(phoneNumber: String): Boolean = accountRepository.existsByPhoneNumber(phoneNumber)

    override fun getVerifyCode(phoneNumber: String): Int? = redisService[phoneNumber] as? Int

    override fun verifyCode(phoneNumber: String) {
        redisService[phoneNumber] = 6782
        redisService.expire(phoneNumber, 30)
    }

    override fun saveAccount(staffId: String?,
                             phoneNumber: String?,
                             socialId: String?,
                             email: String?,
                             password: String?,
                             role: Role?,
                             deviceId: String?,
                             deviceName: String?,
                             platform: String?): AccountResponse? {

        // create and save device
        val devices = arrayListOf<DeviceEntity?>()

        val device = saveDevice(DeviceEntity(
                deviceId = deviceId,
                deviceName = deviceName,
                platform = Platform.valueOf(platform ?: "")))

        devices.add(device)

        // create and save user
        val user = userRepository.insert(UserEntity())

        val result = accountRepository.insert(AccountEntity(
                socialId = socialId,
                email = email,
                phoneNumber = phoneNumber,
                password = encryptPassword(password),
                registerTime = Date.from(Instant.now()),
                collaboratorId = staffId,
                devices = devices,
                role = role,
                info = user
        ))

        // return dto
        return AccountResponse(
                id = result.id,
                socialId = result.socialId,
                email = result.email,
                phoneNumber = result.phoneNumber,
                active = result.active,

                emailVerified = result.emailVerified,
                phoneVerified = result.phoneVerified,

                registerTime = result.registerTime,
                emailVerifiedTime = result.emailVerifiedTime,
                phoneVerifiedTime = result.phoneVerifiedTime,

                collaboratorId = result.collaboratorId,
                role = result.role,
                user = result.info
        )
    }

    override fun saveDevice(device: DeviceEntity): DeviceEntity? = deviceRepository.save(device)

    override fun login(account: AccountEntity): AccountResponse? {

        val result = accountRepository.save(account)

        // insert login history
        loginHistoryRepository.insert(LoginHistoryEntity(
                accountId = result.id,
                deviceId = account.devices?.last(),
                loginTime = Date.from(Instant.now())
        ))

        // return dto
        return AccountResponse(
                id = result.id,
                socialId = result.socialId,
                email = result.email,
                phoneNumber = result.phoneNumber,
                active = result.active,

                emailVerified = result.emailVerified,
                phoneVerified = result.phoneVerified,

                registerTime = result.registerTime,
                emailVerifiedTime = result.emailVerifiedTime,
                phoneVerifiedTime = result.phoneVerifiedTime,

                collaboratorId = result.collaboratorId,
                role = result.role,
                user = result.info
        )
    }

    override fun validatePassword(rawPassword: String?, encodedPassword: String?): Boolean = passwordEncoder.matches(rawPassword, encodedPassword)
    override fun encryptPassword(password: String?): String? = passwordEncoder.encode(password)
}
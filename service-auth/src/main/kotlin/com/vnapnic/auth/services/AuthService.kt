package com.vnapnic.auth.services

import com.vnapnic.auth.controllers.RegisterController
import com.vnapnic.auth.repositories.AccountRepository
import com.vnapnic.auth.repositories.DeviceRepository
import com.vnapnic.auth.repositories.LoginHistoryRepository
import com.vnapnic.auth.repositories.UserRepository
import com.vnapnic.auth.dto.AccountResponse
import com.vnapnic.auth.dto.VerifyType
import com.vnapnic.auth.exception.VerifyCodeExpireException
import com.vnapnic.auth.exception.VerifyCodeNotCorrectException
import com.vnapnic.auth.exception.WrongTooManyTimesException
import com.vnapnic.common.entities.ResultCode
import com.vnapnic.common.service.RedisService
import com.vnapnic.database.entities.AccountEntity
import com.vnapnic.database.entities.DeviceEntity
import com.vnapnic.database.entities.LoginHistoryEntity
import com.vnapnic.database.entities.UserEntity
import com.vnapnic.database.enums.Platform
import com.vnapnic.database.enums.Role
import org.joda.time.DateTime
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

    fun sendVerifyCode(phoneNumber: String, type: VerifyType): Boolean

    fun getVerifyCode(phoneNumber: String, type: VerifyType): Int?

    fun verifyCode(phoneNumber: String,
                   code: Int,
                   deviceId: String?,
                   deviceName: String?,
                   platform: String?,
                   type: VerifyType): AccountResponse?

    fun saveAccount(staffId: String? = null,
                    phoneNumber: String? = null,
                    socialId: String? = null,
                    email: String? = null,
                    password: String? = null,
                    role: Role? = null,
                    deviceId: String? = null,
                    deviceName: String? = null,
                    platform: String? = null): AccountResponse?

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

    val verifyFormat = "%s_%s"
    val verifyCountDownFormat = "%s_%s_countdown"

    override fun findBySocialId(socialId: String?): AccountEntity? = accountRepository.findBySocialId(socialId)
    override fun findByPhoneNumber(phoneNumber: String?): AccountEntity? = accountRepository.findByPhoneNumber(phoneNumber)
    override fun findByEmail(email: String?): AccountEntity? = accountRepository.findByEmail(email)

    override fun existsBySocialId(socialId: String?): Boolean = accountRepository.existsBySocialId(socialId)
    override fun existsByEmail(email: String): Boolean = accountRepository.existsByEmail(email)
    override fun existsByPhoneNumber(phoneNumber: String): Boolean = accountRepository.existsByPhoneNumber(phoneNumber)

    override fun sendVerifyCode(phoneNumber: String, type: VerifyType): Boolean {
        val verifyKey = String.format(verifyFormat, type, phoneNumber)
        val verifyCountDownKey = String.format(verifyCountDownFormat, type, phoneNumber)

        redisService[verifyKey] = 6782
        redisService.expire(verifyKey, 30)

        redisService[verifyCountDownKey] = 5
        redisService.expire(verifyCountDownKey, 30)

        return true
    }

    override fun getVerifyCode(phoneNumber: String, type: VerifyType): Int? {
        val verifyKey = String.format(verifyFormat, type, phoneNumber)
        val verifyCountDownKey = String.format(verifyCountDownFormat, type, phoneNumber)

        log.info("type -----------------> $type")
        log.info("Expire -----------------> ${redisService.getExpire(verifyKey)}")
        log.info("CountDown -----------------> ${redisService[verifyCountDownKey]}")
        return redisService[verifyKey] as? Int
    }

    override fun verifyCode(phoneNumber: String,
                            code: Int,
                            deviceId: String?,
                            deviceName: String?,
                            platform: String?,
                            type: VerifyType): AccountResponse? {

        val verifyKey = String.format(verifyFormat, type, phoneNumber)
        val verifyCountDownKey = String.format(verifyCountDownFormat, type, phoneNumber)

        val verifyCode = redisService[verifyKey]
        val countDown = (redisService[verifyCountDownKey] as? Int ?: 0)

        if ((redisService.getExpire(verifyKey) ?: -2) <= 0L)
            throw VerifyCodeExpireException()

        if (countDown <= 0)
            throw WrongTooManyTimesException()

        if (verifyCode != code) {
            redisService[verifyCountDownKey] = countDown - 1
            throw VerifyCodeNotCorrectException()
        }

        val result = findByPhoneNumber(phoneNumber)

        val account: AccountResponse? = if (result != null)
            convertAccountEntityToResponse(result)
        else
            saveAccount(phoneNumber = phoneNumber, deviceId = deviceId, deviceName = deviceName, platform = platform)

        return verifyPhoneNumber(account?.id)
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
                password = if (password.isNullOrEmpty()) null else encryptPassword(password),
                registerTime = Date.from(Instant.now()),
                collaboratorId = staffId,
                devices = devices,
                role = role,
                info = user
        ))

        // return dto
        return convertAccountEntityToResponse(result)
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
        return convertAccountEntityToResponse(result)
    }

    override fun validatePassword(rawPassword: String?, encodedPassword: String?): Boolean = passwordEncoder.matches(rawPassword, encodedPassword)
    override fun encryptPassword(password: String?): String? = passwordEncoder.encode(password)

    private fun verifyPhoneNumber(accountId: String?): AccountResponse? {
        if (accountId == null)
            return null

        val account = accountRepository.findById(accountId).get()
        account.phoneVerified = true
        account.phoneVerifiedTime = Date.from(Instant.now())

        val result = accountRepository.save(account)
        return convertAccountEntityToResponse(result)
    }

    private fun verifyEmail(accountId: String?): AccountResponse? {
        if (accountId == null)
            return null

        val account = accountRepository.findById(accountId).get()
        account.emailVerified = true
        account.emailVerifiedTime = Date.from(Instant.now())

        val result = accountRepository.save(account)
        return convertAccountEntityToResponse(result)
    }

    private fun convertAccountEntityToResponse(entity: AccountEntity?): AccountResponse? = AccountResponse(
            id = entity?.id,
            socialId = entity?.socialId,
            email = entity?.email,
            phoneNumber = entity?.phoneNumber,
            active = entity?.active,

            emailVerified = entity?.emailVerified,
            phoneVerified = entity?.phoneVerified,

            registerTime = entity?.registerTime,
            emailVerifiedTime = entity?.emailVerifiedTime,
            phoneVerifiedTime = entity?.phoneVerifiedTime,

            collaboratorId = entity?.collaboratorId,
            role = entity?.role,
            user = entity?.info
    )
}
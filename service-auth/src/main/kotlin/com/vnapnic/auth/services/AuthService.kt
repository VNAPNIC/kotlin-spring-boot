package com.vnapnic.auth.services

import com.vnapnic.auth.controllers.SignUpController
import com.vnapnic.common.dto.AccountResponse
import com.vnapnic.auth.dto.VerifyType
import com.vnapnic.auth.exception.VerifyCodeExpireException
import com.vnapnic.auth.exception.VerifyCodeIncorrectException
import com.vnapnic.auth.exception.WrongTooManyTimesException
import com.vnapnic.auth.repositories.AccountRepository
import com.vnapnic.auth.repositories.DeviceRepository
import com.vnapnic.auth.repositories.LoginHistoryRepository
import com.vnapnic.auth.repositories.UserRepository
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
import java.util.concurrent.ThreadLocalRandom

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
                   code: Int?,
                   deviceId: String?,
                   deviceName: String?,
                   platform: Platform = Platform.OTHER,
                   type: VerifyType,
                   role: Role): AccountResponse?

    fun saveAccount(staffId: String? = null,
                    phoneNumber: String? = null,
                    socialId: String? = null,
                    email: String? = null,
                    password: String? = null,
                    role: Role? = null,
                    deviceId: String? = null,
                    deviceName: String? = null,
                    platform: Platform = Platform.OTHER): AccountResponse?

    fun saveDevice(device: DeviceEntity): DeviceEntity?

    fun login(account: AccountEntity): AccountResponse?
    fun logout(accountId: String)

    fun validatePassword(rawPassword: String?, encodedPassword: String?): Boolean
    fun encryptPassword(password: String?): String?
}

@Service
class AuthServiceImpl : AuthService {
    private val log = LoggerFactory.getLogger(SignUpController::class.java)

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
    val verifyIncorrectCountFormat = "%s_%s_incorrect_count"

    override fun findBySocialId(socialId: String?): AccountEntity? = accountRepository.findBySocialId(socialId)
    override fun findByPhoneNumber(phoneNumber: String?): AccountEntity? = accountRepository.findByPhoneNumber(phoneNumber)
    override fun findByEmail(email: String?): AccountEntity? = accountRepository.findByEmail(email)

    override fun existsBySocialId(socialId: String?): Boolean = accountRepository.existsBySocialId(socialId)
    override fun existsByEmail(email: String): Boolean = accountRepository.existsByEmail(email)
    override fun existsByPhoneNumber(phoneNumber: String): Boolean = accountRepository.existsByPhoneNumber(phoneNumber)

    override fun sendVerifyCode(phoneNumber: String, type: VerifyType): Boolean {
        val verifyKey = String.format(verifyFormat, type, phoneNumber)
        val verifyCountDownKey = String.format(verifyIncorrectCountFormat, type, phoneNumber)
        val code = ThreadLocalRandom.current().nextInt(100000, 1000000)
        redisService[verifyKey] = code
        redisService.expire(verifyKey, 30)

        redisService[verifyCountDownKey] = 3
        redisService.expire(verifyCountDownKey, 30)

        return true
    }

    override fun getVerifyCode(phoneNumber: String, type: VerifyType): Int? {
        val verifyKey = String.format(verifyFormat, type, phoneNumber)
        return redisService[verifyKey] as? Int
    }

    override fun verifyCode(phoneNumber: String,
                            code: Int?,
                            deviceId: String?,
                            deviceName: String?,
                            platform: Platform,
                            type: VerifyType,
                            role: Role): AccountResponse? {

        val verifyKey = String.format(verifyFormat, type, phoneNumber)
        val incorrectCountKey = String.format(verifyIncorrectCountFormat, type, phoneNumber)

        val verifyCode = redisService[verifyKey]
        var incorrectCount = (redisService[incorrectCountKey] as? Int ?: 0)

        if ((redisService.getExpire(verifyKey) ?: -2) <= 0L)
            throw VerifyCodeExpireException()

        if (verifyCode != code || code == null || code < 100000 || code > 999999) {
            incorrectCount--
            redisService[incorrectCountKey] = incorrectCount
            if (incorrectCount <= 0)
                throw WrongTooManyTimesException()

            throw VerifyCodeIncorrectException(redisService[incorrectCountKey] as? Int ?: 3)
        }

        val result = findByPhoneNumber(phoneNumber)

        val account: AccountResponse? = if (result != null)
            AccountResponse.from(result)
        else
            saveAccount(phoneNumber = phoneNumber, deviceId = deviceId, deviceName = deviceName, platform = platform, role = role)

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
                             platform: Platform): AccountResponse? {

        // create and save device
        val devices = arrayListOf<DeviceEntity?>()

        val device = saveDevice(DeviceEntity(
                deviceId = deviceId,
                deviceName = deviceName,
                platform = platform))

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
        return AccountResponse.from(result)
    }

    override fun saveDevice(device: DeviceEntity): DeviceEntity? = deviceRepository.save(device)

    override fun login(account: AccountEntity): AccountResponse? {

        account.active = true
        val result = accountRepository.save(account)

        // insert login history
        loginHistoryRepository.insert(LoginHistoryEntity(
                accountId = result.id,
                deviceId = account.devices?.last(),
                loginTime = Date.from(Instant.now())
        ))

        // return dto
        return AccountResponse.from(result)
    }

    override fun logout(accountId: String){
        val result = accountRepository.findById(accountId).get()
        result.active = false
        accountRepository.save(result)
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
        return AccountResponse.from(result)
    }

    private fun verifyEmail(accountId: String?): AccountResponse? {
        if (accountId == null)
            return null

        val account = accountRepository.findById(accountId).get()
        account.emailVerified = true
        account.emailVerifiedTime = Date.from(Instant.now())

        val result = accountRepository.save(account)
        return AccountResponse.from(result)
    }
}
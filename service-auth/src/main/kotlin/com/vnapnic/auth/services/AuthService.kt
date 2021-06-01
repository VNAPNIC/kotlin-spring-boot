package com.vnapnic.auth.services

import com.vnapnic.auth.repositories.AccountRepository
import com.vnapnic.auth.repositories.DeviceRepository
import com.vnapnic.auth.repositories.LoginHistoryRepository
import com.vnapnic.auth.repositories.UserRepository
import com.vnapnic.common.dto.AccountDTO
import com.vnapnic.database.beans.AccountBean
import com.vnapnic.database.beans.DeviceBean
import com.vnapnic.database.beans.LoginHistoryBean
import com.vnapnic.database.beans.UserBean
import com.vnapnic.database.enums.Platform
import com.vnapnic.database.enums.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

interface AuthService {

    fun findBySocialId(socialId: String?): AccountBean?
    fun findByPhoneNumber(phoneNumber: String?): AccountBean?
    fun findByEmail(email: String?): AccountBean?

    fun existsBySocialId(socialId: String?): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean

    fun saveAccount(staffId: String?,
                    phoneNumber: String?,
                    socialId: String?,
                    email: String?,
                    password: String?,
                    role: Role?,
                    deviceId: String?,
                    deviceName: String?,
                    platform: String?): AccountDTO?

    fun saveDevice(device: DeviceBean): DeviceBean?

    fun login(account: AccountBean): AccountDTO?

    fun validatePassword(rawPassword: String?, encodedPassword: String?): Boolean
    fun encryptPassword(password: String?): String?
}

@Service
class AuthServiceImpl : AuthService {

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

    override fun findBySocialId(socialId: String?): AccountBean? = accountRepository.findBySocialId(socialId)
    override fun findByPhoneNumber(phoneNumber: String?): AccountBean? = accountRepository.findByPhoneNumber(phoneNumber)
    override fun findByEmail(email: String?): AccountBean? = accountRepository.findByEmail(email)

    override fun existsBySocialId(socialId: String?): Boolean = accountRepository.existsBySocialId(socialId)
    override fun existsByEmail(email: String): Boolean = accountRepository.existsByEmail(email)
    override fun existsByPhoneNumber(phoneNumber: String): Boolean = accountRepository.existsByPhoneNumber(phoneNumber)

    override fun saveAccount(staffId: String?,
                             phoneNumber: String?,
                             socialId: String?,
                             email: String?,
                             password: String?,
                             role: Role?,
                             deviceId: String?,
                             deviceName: String?,
                             platform: String?): AccountDTO? {

        // create and save device
        val devices = arrayListOf<DeviceBean?>()

        val device = saveDevice(DeviceBean(
                deviceId = deviceId,
                deviceName = deviceName,
                platform = Platform.valueOf(platform ?: "")))

        devices.add(device)

        // create and save user
        val user = userRepository.insert(UserBean())

        val result = accountRepository.insert(AccountBean(
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
        return AccountDTO(
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

    override fun saveDevice(device: DeviceBean): DeviceBean? = deviceRepository.save(device)

    override fun login(account: AccountBean): AccountDTO? {

        val result = accountRepository.save(account)

        // insert login history
        loginHistoryRepository.insert(LoginHistoryBean(
                accountId = result.id,
                deviceId = account.devices?.last(),
                loginTime = Date.from(Instant.now())
        ))

        // return dto
        return AccountDTO(
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
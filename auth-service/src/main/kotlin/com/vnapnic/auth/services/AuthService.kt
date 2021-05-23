package com.vnapnic.auth.services

import com.vnapnic.auth.repositories.AccountRepository
import com.vnapnic.auth.repositories.DeviceRepository
import com.vnapnic.auth.repositories.UserRepository
import com.vnapnic.common.db.Account
import com.vnapnic.common.db.Device
import com.vnapnic.common.db.User
import com.vnapnic.common.dto.AccountDTO
import com.vnapnic.common.enums.Platform
import com.vnapnic.common.enums.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface AuthService {
    fun findBySocialId(socialId: String?): Account?
    fun findByEmail(email: String?): Account?

    fun existsBySocialId(socialId: String?): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean

    fun save(staffId: String?,
             phoneNumber: String?,
             socialId: String?,
             email: String?,
             password: String?,
             role: Role?,
             deviceId: String?,
             deviceName: String?,
             platform: String?): AccountDTO?

    fun updateDevices(rawAccount: Account, device: Device) : AccountDTO?

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
    lateinit var passwordEncoder: PasswordEncoder

    override fun findBySocialId(socialId: String?): Account? = accountRepository.findBySocialId(socialId)

    override fun findByEmail(email: String?): Account? = accountRepository.findByEmail(email)

    override fun existsBySocialId(socialId: String?): Boolean = accountRepository.existsBySocialId(socialId)
    override fun existsByEmail(email: String): Boolean = accountRepository.existsByEmail(email)
    override fun existsByPhoneNumber(phoneNumber: String): Boolean = accountRepository.existsByPhoneNumber(phoneNumber)

    override fun save(staffId: String?,
                      phoneNumber: String?,
                      socialId: String?,
                      email: String?,
                      password: String?,
                      role: Role?,
                      deviceId: String?,
                      deviceName: String?,
                      platform: String?): AccountDTO? {

        // create and save device
        val devices = arrayListOf<Device?>()
        val device = deviceRepository.save(Device(
                deviceId = deviceId,
                deviceName = deviceName,
                platform = Platform.valueOf(platform ?: "")))
        devices.add(device)

        // create and save user
        val user = userRepository.save(User())

        val account = accountRepository.save(Account(
                phoneNumber = phoneNumber,
                socialId = socialId,
                email = email,
                password = encryptPassword(password),
                staffId = staffId,
                role = role,
                info = user,
                devices = devices
        ))

        return AccountDTO(
                socialId = account.socialId,
                email = account.email,
                phoneNumber = account.phoneNumber,
                active = account.active,
                verified = account.emailVerified,
                staffId = account.staffId,
                role = account.role,
                user = account.info,
                devices = account.devices
        )
    }

    override fun updateDevices(rawAccount: Account, device: Device): AccountDTO? {
        deviceRepository.save(device)
        val account =  accountRepository.save(rawAccount)
        return AccountDTO(
                id = account._id,
                socialId = account.socialId,
                email = account.email,
                active = account.active,
                verified = account.emailVerified,
                role = account.role,
                devices = account.devices
        )
    }

    override fun validatePassword(rawPassword: String?, encodedPassword: String?): Boolean = passwordEncoder.matches(rawPassword, encodedPassword)
    override fun encryptPassword(password: String?): String? = passwordEncoder.encode(password)
}
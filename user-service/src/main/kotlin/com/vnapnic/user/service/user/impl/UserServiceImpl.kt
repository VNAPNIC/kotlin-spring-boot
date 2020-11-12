package com.vnapnic.user.service.user.impl

import com.vnapnic.user.model.evironment.EnvironmentParams
import com.vnapnic.user.model.user.entity.User
import com.vnapnic.user.repository.UserRepository
import com.vnapnic.user.service.user.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
        private val environment: EnvironmentParams,
        private val repository: UserRepository,
        private val redisTemplate: StringRedisTemplate,
        private val tokenServices: ConsumerTokenServices
) : UserService {

    @Value("\${spring.mail.username}")
    private val from: String? = null

    @CachePut(value = ["user-cache"], key="#user.userId", condition = "#result != null")
    override fun saveUser(user: User): User {
        return repository.save(user)
    }

    override fun checkNewPasswordValid(password: String): Boolean {
        return password.matches("""\w{6,30}""".toRegex())
    }

    @Cacheable(value = ["username-cache"], key="#username", condition = "#result == false")
    override fun checkNewUsernameValid(username: String): Boolean {
        return username.matches("""\S{4,30}""".toRegex()) && repository.userExists(username)
    }
}
package com.vnapnic.user.service.user

import com.vnapnic.user.model.user.entity.CustomUserDetails
import com.vnapnic.user.model.user.entity.User
import com.vnapnic.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*

class CustomUserDetailsService constructor(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val dbUser: Optional<User> = userRepository.findByUsername(username)
        return dbUser.map { user -> CustomUserDetails(user) }
                .orElseThrow { UsernameNotFoundException("Couldn't find a matching user username in the database for $username") }
    }

    fun loadUserById(id: Long): UserDetails {
        val dbUser = userRepository.findById(id)
        return dbUser.map { user -> CustomUserDetails(user) }
                .orElseThrow { UsernameNotFoundException("Couldn't find a matching user id in the database for $id") }
    }
}
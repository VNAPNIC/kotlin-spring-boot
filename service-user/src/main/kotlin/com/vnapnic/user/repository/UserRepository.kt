package com.vnapnic.user.repository

import com.vnapnic.user.model.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun existsByUsername(username: String?): Boolean
    fun existsBy(): Boolean
    fun findByUsername(username: String): Optional<User>
}
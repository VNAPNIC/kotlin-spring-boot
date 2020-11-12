package com.vnapnic.user.repository

import com.vnapnic.user.model.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun userExists(username: String): Boolean
}
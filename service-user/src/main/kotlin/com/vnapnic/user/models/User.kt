package com.vnapnic.user.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class User(
        val username: String,
        val password: String,
        val firstName: String,
        val lastName: String,
        val email: String,
        @Id @GeneratedValue var id: Long? = null
)
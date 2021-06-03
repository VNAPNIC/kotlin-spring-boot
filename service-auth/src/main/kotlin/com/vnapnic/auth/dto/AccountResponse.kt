package com.vnapnic.auth.dto

import com.google.common.reflect.TypeToken
import com.vnapnic.common.entities.Response
import com.vnapnic.database.enums.Role
import com.vnapnic.database.entities.UserEntity
import java.util.*
import kotlin.reflect.KClass

data class AccountResponse(var id: String? = null,
                           val socialId: String? = null,
                           val phoneNumber: String? = null,
                           val email: String? = null,
                           val active: Boolean?,
                           val emailVerified: Boolean?,
                           val phoneVerified: Boolean?,
                           val registerTime: Date? = null,
                           val emailVerifiedTime: Date? = null,
                           val phoneVerifiedTime: Date? = null,
                           val collaboratorId: String? = null,
                           val role: Role? = Role.UNKNOWN,
                           val user: UserEntity? = null
)

fun <T : Any> test(t: T): KClass<out T> = t::class
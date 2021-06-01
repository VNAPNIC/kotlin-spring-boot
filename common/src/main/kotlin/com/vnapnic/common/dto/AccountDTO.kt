package com.vnapnic.common.dto

import com.vnapnic.database.enums.Role
import com.vnapnic.database.beans.UserBean
import java.util.*


open class AccountDTO(var id: String? = null,
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
                      val user: UserBean? = null
)
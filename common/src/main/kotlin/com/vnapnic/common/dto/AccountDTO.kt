package com.vnapnic.common.dto

import com.vnapnic.common.db.Device
import com.vnapnic.common.enums.Role
import com.vnapnic.common.db.User


open class AccountDTO(val id: String? = null,
                      val socialId: String? = null,
                      val phoneNumber: String? = null,
                      val email: String? = null,
                      val active: Boolean?,
                      val verified: Boolean?,
                      val staffId: String? = null,
                      val role: Role? = Role.UNKNOWN,
                      val user: User? = null,
                      val devices: ArrayList<Device?>? = null
)
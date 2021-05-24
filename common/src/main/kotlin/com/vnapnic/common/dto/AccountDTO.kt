package com.vnapnic.common.dto

import com.vnapnic.database.enums.Role
import com.vnapnic.database.beans.DeviceBean
import com.vnapnic.database.beans.UserBean


open class AccountDTO(var id: String? = null,
                      val socialId: String? = null,
                      val phoneNumber: String? = null,
                      val email: String? = null,
                      val active: Boolean?,
                      val verified: Boolean?,
                      val staffId: String? = null,
                      val role: Role? = Role.UNKNOWN,
                      val user: UserBean? = null,
                      val devices: ArrayList<DeviceBean?>? = null
)
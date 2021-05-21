package com.vnapnic.common.dto

import com.vnapnic.common.db.Device
import com.vnapnic.common.enums.Role
import com.vnapnic.common.db.User


open class AccountDTO(var id: String? = null,
                      var socialId: String? = null,
                      var phoneNumber: String? = null,
                      var email: String? = null,
                      var active: Boolean?,
                      var verified: Boolean?,
                      var staffId: String? = null,
                      var role: Role? = Role.UNKNOWN,
                      val user: User? = null,
                      val devices: ArrayList<Device?>? = null
)
package com.vnapnic.common.dto

import com.vnapnic.common.db.Role
import com.vnapnic.common.db.User


open class AccountDTO(var id: String? = null,
                      var socialId: String? = null,
                      var email: String? = null,
                      var active: Boolean?,
                      var verified: Boolean?,
                      var staffId: String? = null,
                      var role: Role? = Role.UNKNOWN,
                      val user: User? = null
){
    override fun toString(): String {
        return "AccountDTO(id=$id, socialId=$socialId, email=$email, active=$active, verified=$verified, staffId=$staffId)"
    }
}
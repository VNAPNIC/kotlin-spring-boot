package com.vnapnic.common.dto

import com.vnapnic.common.db.Role


open class AccountDTO(var id: String? = null,
                      var socialId: String? = null,
                      var email: String? = null,
                      var active: Boolean?,
                      var verified: Boolean?,
                      var staffId: String? = null,
                      var role: Role? = Role.UNKNOWN
){
    override fun toString(): String {
        return "AccountDTO(id=$id, socialId=$socialId, email=$email, active=$active, verified=$verified, staffId=$staffId)"
    }
}
package com.vnapnic.common.dto


open class AccountDTO(var id: String? = null,
                      var socialId: String? = null,
                      var email: String? = null,
                      var active: Boolean?,
                      var verified: Boolean?,
                      var staffId: String? = null
){
    override fun toString(): String {
        return "AccountDTO(id=$id, socialId=$socialId, email=$email, active=$active, verified=$verified, staffId=$staffId)"
    }
}
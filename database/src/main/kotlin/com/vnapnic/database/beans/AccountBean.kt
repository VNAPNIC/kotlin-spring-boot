package com.vnapnic.database.beans

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.vnapnic.database.enums.Role
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "account")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccountBean(
        @JsonProperty("_id")
        @Id
        var _id: String = "",
        @JsonProperty("phoneNumber")
        var phoneNumber: String? = null,
        @JsonProperty("socialId")
        var socialId: String? = null,
        @JsonProperty("email")
        var email: String? = null,
        @JsonProperty("password")
        var password: String? = null,
        @JsonProperty("cccdFront")
        var cccdFront: String? = null,
        @JsonProperty("cccdBack")
        var cccdBack: String? = null,
        @JsonProperty("active")
        var active: Boolean = false,
        @JsonProperty("blocked")
        var blocked: Boolean = false,
        @JsonProperty("emailVerified") // Email verification passed
        var emailVerified: Boolean = false,
        @JsonProperty("staffId")
        var staffId: String? = null,
        @JsonProperty("devices")
        var devices: ArrayList<DeviceBean?>? = null,
        @JsonProperty("role")
        var role: Role? = Role.UNKNOWN,
        @JsonProperty("info")
        @DBRef
        var info: UserBean? = null
) {
    companion object {
        @Transient
        const val SEQUENCE_NAME = "staff_id_sequence"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AccountBean

        if (_id != other._id) return false

        return true
    }

    override fun hashCode(): Int {
        return _id.hashCode()
    }
}
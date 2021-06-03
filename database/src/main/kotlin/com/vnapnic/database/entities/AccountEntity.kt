package com.vnapnic.database.entities

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.vnapnic.database.enums.Role
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import kotlin.collections.ArrayList

@Document(collection = "account")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccountEntity(
        @Id
        @JsonProperty("_id")
        var id: String? = null,
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
        @JsonProperty("emailVerified") // Email verification passed
        var emailVerified: Boolean = false,
        @JsonProperty("emailVerified") // Email verification passed
        var phoneVerified: Boolean = false,
        @JsonProperty("registerTime")
        var registerTime: Date? = null,
        @JsonProperty("emailVerifiedTime")
        var emailVerifiedTime: Date? = null,
        @JsonProperty("phoneVerifiedTime")
        var phoneVerifiedTime: Date? = null,
        @JsonProperty("collaboratorId")
        var collaboratorId: String? = null,
        @JsonProperty("devices")
        var devices: ArrayList<DeviceEntity?>? = null,
        @JsonProperty("role")
        var role: Role? = Role.UNKNOWN,
        @JsonProperty("info")
        @DBRef
        var info: UserEntity? = null
) {
    companion object {
        @Transient
        const val SEQUENCE_NAME = "staff_id_sequence"
    }
}
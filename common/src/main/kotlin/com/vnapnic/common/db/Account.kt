package com.vnapnic.common.db

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "account")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        "id",
        "username",
        "password",
        "email",
        "active")
data class Account(

        @JsonProperty("id")
        @Id
        var id: String = "",
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
        @JsonProperty("verified") // Email verification passed
        var verified: Boolean = false,
        @JsonProperty("verified") // Email verification passed

        var staffId: String = ""
) {
    companion object {
        @Transient
        const val SEQUENCE_NAME = "account_sequence"
    }
}
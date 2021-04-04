package com.vnapnic.auth.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.mongodb.core.mapping.Document


@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Account(
        @JsonProperty("id")
        var id: String? = null,

        @JsonProperty("socialId")
        var socialId: String? = null,

        @JsonProperty("username")
        var username: String? = null,

        @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
        var password: String? = null,

        @JsonProperty("email")
        var email: String? = null,

        @JsonProperty("active")
        var active: Boolean? = null,

        @JsonProperty("verified")
        var verified: Boolean? = null
)
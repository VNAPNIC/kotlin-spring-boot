package com.vnapnic.common.db

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "account")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class User(
        @JsonProperty("id")
        @Id
        var id: String = "",

)
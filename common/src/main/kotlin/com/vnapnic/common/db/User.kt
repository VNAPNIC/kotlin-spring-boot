package com.vnapnic.common.db

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.vnapnic.common.db.files.AvatarInfo
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class User(
        @JsonProperty("id")
        @Id
        var id: String = "",
        @JsonProperty("firstName")
        var firstName: String? = null,
        @JsonProperty("lastName")
        var lastName: String? = null,
        @JsonProperty("weight")
        var weight: Double? = 0.0, // grams
        @JsonProperty("height")
        var height: Double? = 0.0, // cm
        @JsonProperty("gender")
        var gender: Gender? = Gender.OTHER,
        @JsonProperty("description")
        var description: String? = null
) {
    companion object {
        @Transient
        const val SEQUENCE_NAME = "user_sequence"
    }
}
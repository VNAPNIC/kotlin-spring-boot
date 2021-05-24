package com.vnapnic.database.beans

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.vnapnic.database.beans.files.AvatarInfoBean
import com.vnapnic.database.enums.Gender
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserBean(
        @JsonProperty("_id")
        @Id
        var _id: String? = null,
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
        var description: String? = null,
        @JsonProperty("avatar")
        var avatar: AvatarInfoBean? = null
)
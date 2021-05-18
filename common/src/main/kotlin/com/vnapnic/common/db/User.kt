package com.vnapnic.common.db

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.vnapnic.common.db.files.AvatarInfo
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class User(@JsonProperty("id")
                    @Id
                    var id: String = "",
                var firstName: String? = null,
                var lastName: String? = null,
                var weight: Double? = 0.0, // grams
                var height: Double? = 0.0, // cm
                var gender: Gender? = Gender.OTHER,
                var avatar: AvatarInfo,
                var description: String? = null,
                var cleanTools: Array<CleanTool>? = arrayOf()) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as User
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
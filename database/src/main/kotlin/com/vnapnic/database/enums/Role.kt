package com.vnapnic.database.enums

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "role")
@JsonInclude(JsonInclude.Include.NON_NULL)
enum class Role {
    STAFF,
    CUSTOMER,
    ADMIN,
    UNKNOWN
}
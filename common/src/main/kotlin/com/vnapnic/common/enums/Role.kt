package com.vnapnic.common.enums

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "role")
@JsonInclude(JsonInclude.Include.NON_NULL)
enum class Role(val value: Int) {
    STAFF(0),
    CUSTOMER(1),
    ADMIN(2),
    UNKNOWN(3)
}
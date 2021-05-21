package com.vnapnic.common.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "gender")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Platform(val value: String) {
    ANDROID("ANDROID"),
    IOS("IOS"),
    WEBSITE("WEBSITE"),
    OTHER("OTHER")
}
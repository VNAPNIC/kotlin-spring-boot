package com.vnapnic.database.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "gender")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Platform(val value: String) {
    @JsonProperty("android")
    ANDROID("android"),
    @JsonProperty("ios")
    IOS("ios"),
    @JsonProperty("website")
    WEBSITE("website"),
    @JsonProperty("other")
    OTHER("other")
}
package com.vnapnic.database.entities

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "loginHistory")
data class LoginHistoryEntity(
        @JsonProperty("accountId")
        val accountId: String? = null,
        @JsonProperty("deviceInfo")
        val deviceInfo: DeviceEntity? = null,
        @JsonProperty("loginTime")
        val loginTime: Date? = null)
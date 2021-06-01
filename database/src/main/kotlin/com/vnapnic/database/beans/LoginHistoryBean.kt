package com.vnapnic.database.beans

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "loginHistory")
data class LoginHistoryBean(
        @JsonProperty("accountId")
        val accountId: String? = null,
        @JsonProperty("deviceId")
        val deviceId: DeviceBean? = null,
        @JsonProperty("loginTime")
        val loginTime: Date? = null)
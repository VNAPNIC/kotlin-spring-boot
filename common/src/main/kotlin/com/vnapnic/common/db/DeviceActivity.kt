package com.vnapnic.common.db

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class DeviceActivity(
        @JsonProperty("accountId")
        val accountId: String? = null,
        @JsonProperty("deviceId")
        val deviceId: Device? = null,
        @JsonProperty("loginTime")
        val loginTime: Date? = null,
        @JsonProperty("registerTime")
        val registerTime: Date? = null)
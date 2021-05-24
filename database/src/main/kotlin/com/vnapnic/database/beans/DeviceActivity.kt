package com.vnapnic.database.beans

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class DeviceActivity(
        @JsonProperty("accountId")
        val accountId: String? = null,
        @JsonProperty("deviceId")
        val deviceBeanId: DeviceBean? = null,
        @JsonProperty("loginTime")
        val loginTime: Date? = null,
        @JsonProperty("registerTime")
        val registerTime: Date? = null)
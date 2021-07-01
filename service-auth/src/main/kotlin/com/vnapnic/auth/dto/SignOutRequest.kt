package com.vnapnic.auth.dto

import com.vnapnic.database.enums.Platform

class SignOutRequest(
        val deviceName: String?,
        val deviceId: String?,
        val platform: Platform
)
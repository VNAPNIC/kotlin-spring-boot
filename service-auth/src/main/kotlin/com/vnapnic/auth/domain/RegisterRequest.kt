package com.vnapnic.auth.domain

data class RegisterRequest(
        val code: String?,
        val phoneNumber: String?,
        val socialId: String?,
        val email: String?,
        val password: String?,
        val deviceName: String?,
        val deviceId: String?,
        val platform: String?
)
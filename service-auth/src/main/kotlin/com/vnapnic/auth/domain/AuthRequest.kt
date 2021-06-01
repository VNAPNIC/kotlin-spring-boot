package com.vnapnic.auth.domain

class AuthRequest(
        val email: String?,
        val phoneNumber: String?,
        val password: String?,
        val deviceName: String?,
        val deviceId: String?,
        val platform: String?
)
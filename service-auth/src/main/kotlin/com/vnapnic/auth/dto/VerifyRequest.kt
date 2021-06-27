package com.vnapnic.auth.dto

data class SendVerifyCodeRequest(
        val email: String?,
        val phoneNumber: String?,
        val dialCode: String?,
        val numCode: String?,
        val alpha2Code: String?,
        val alpha3Code: String?,
        val enShortName: String?,
        val nationality: String?,
)

data class VerifyCodeRequest(
        val email: String?,
        val phoneNumber: String?,
        val dialCode: String?,
        val numCode: String?,
        val alpha2Code: String?,
        val alpha3Code: String?,
        val enShortName: String?,
        val nationality: String?,
        val verifyCode: Int?
)
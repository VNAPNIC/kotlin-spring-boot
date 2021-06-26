package com.vnapnic.auth.dto

enum class VerifyType(val value: Int) {
    PHONE_NUMBER(0),
    EMAIL(1)
}

data class VerifyCodeRequest(
        val phoneNumber: String?,
        val dialCode: String?,
        val numCode: String?,
        val alpha2Code: String?,
        val alpha3Code: String?,
        val enShortName: String?,
        val nationality: String?,
        val VerifyType: VerifyType?,
)

data class GetVerifyCodeRequest(
        val phoneNumber: String?,
        val dialCode: String?,
        val numCode: String?,
        val alpha2Code: String?,
        val alpha3Code: String?,
        val enShortName: String?,
        val nationality: String?,
        val VerifyType: VerifyType?,
)
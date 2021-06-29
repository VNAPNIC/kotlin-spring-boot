package com.vnapnic.auth.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.vnapnic.database.enums.Platform
import com.vnapnic.database.enums.Role

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class VerifyType {
    PHONE_NUMBER,
    EMAIL,
    PASSWORD
}

data class GetVerifyCodeRequest(
        val email: String?,
        val phoneNumber: String?,
        val dialCode: String?,
        val numCode: String?,
        val alpha2Code: String?,
        val alpha3Code: String?,
        val enShortName: String?,
        val nationality: String?,
        val type: VerifyType
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
        val deviceName: String?,
        val deviceId: String?,
        val platform: Platform,
        val type: VerifyType,
        val verifyCode: Int?,
        val role: Role,
)
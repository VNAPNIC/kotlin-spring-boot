package com.vnapnic.auth.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.vnapnic.database.enums.Platform
import com.vnapnic.database.enums.Role

/**
 * Determine what type of code to verify
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class VerifyTypeCode {
    PHONE_NUMBER,
    EMAIL,
    PASSWORD
}

/**
 * To determine if you want to get the code to register or log in
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class SendVerifyType {
    LOGIN,
    REGISTER,
}

data class RequestSendToVerifyCode(
        val email: String?,
        val phoneNumber: String?,
        val dialCode: String?,
        val numCode: String?,
        val alpha2Code: String?,
        val alpha3Code: String?,
        val enShortName: String?,
        val nationality: String?,
        val typeCode: VerifyTypeCode,
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
        val typeCode: VerifyTypeCode,
        val verifyCode: Int?,
        val role: Role,
)
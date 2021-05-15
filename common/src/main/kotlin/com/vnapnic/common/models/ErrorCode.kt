package com.vnapnic.common.models

enum class ErrorCode(val code: Long, val message: String) {
    EMAIL_PASSWORD_NOT_CORRECT(1001, "Email/Password is not correct."),
    SOCIAL_PASSWORD_NOT_CORRECT(1002, "Social/Password is not correct."),

    PASSWORD_IS_NULL_BLANK(1003, "Password is null or blank."),

    EMAIL_IS_NULL_BLANK(1004, "Email is null or blank."),
    EMAIL_IS_EXISTS(1005, "Email is exists."),

    SOCIAL_IS_EXISTS(1006, "Social is exists."),

    PHONE_NUMBER_IS_NULL_BLANK(1007, "Phone number is null or blank."),
    PHONE_NUMBER_IS_EXISTS(1008, "Phone number is exists."),

    CODE_NOT_CORRECT(1009, "CODE is not correct."),

    FILE_TOO_LARGE(1010,"File too large."),

    FILE_UPLOAD_FAIL(1011,"Upload file fail.")
}
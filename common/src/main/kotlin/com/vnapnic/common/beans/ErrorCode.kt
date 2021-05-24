package com.vnapnic.common.beans

enum class ErrorCode(val code: Long, val message: String) {

    SERVER_UNKNOWN_ERROR(1000, "Server unknown error."),

    EMAIL_PASSWORD_NOT_CORRECT(1001, "Email/Password is not correct."),
    SOCIAL_PASSWORD_NOT_CORRECT(1002, "Social/Password is not correct."),

    PASSWORD_IS_NULL_BLANK(1003, "Password is null or blank."),

    EMAIL_IS_NULL_BLANK(1004, "Email is null or blank."),
    EMAIL_IS_EXISTS(1005, "Email is exists."),

    SOCIAL_IS_EXISTS(1006, "Social is exists."),

    PHONE_NUMBER_IS_NULL_BLANK(1007, "Phone number is null or blank."),
    PHONE_NUMBER_IS_EXISTS(1008, "Phone number is exists."),

    CODE_NOT_CORRECT(1009, "CODE is not correct."),

    FILE_TOO_LARGE(1010, "File too large."),

    FILE_UPLOAD_FAIL(1011, "Upload file fail."),

    UNSUPPORTED_MEDIA_TYPE(1012, "Unsupported Media Type."),

    UNSUPPORTED_DEVICE(1013, "Unsupported device."),

    PHONE_NUMBER_WRONG_FORMAT(1014, "Wrong phone number format."),

    EMAIL_WRONG_FORMAT(1015, "Wrong email format.")
}
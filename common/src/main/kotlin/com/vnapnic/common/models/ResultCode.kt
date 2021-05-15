package com.vnapnic.common.models

enum class ResultCode(val code: Long, val message: String) {
    SUCCESS(200, "Successful"),
    FAILED(500, "Failed"),
    VALIDATE_FAILED(404, "Verification failed"),
    UNAUTHORIZED(401, "Not logged in yet or token has expired"),
    FORBIDDEN(403, "No relevant permissions"),
    EXPECTATION_FAILED(417, "Expectation Failed");
}

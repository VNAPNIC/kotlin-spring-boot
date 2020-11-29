package com.vnapnic.user.exception

enum class ExceptionEnum(val error: String, val message: String, val status: Int) {
    REQUEST_FORMAT_ERROR("invalid_request", "Invalid request fields", 400),
    USERNAME_PASSWORD_ERROR("invalid_client", "Username or password entered by the user is incorrect", 400),
    SAFE_VERIFY_CODE_ERROR("forbidden", "Security verification code entered by the user is incorrect", 403),
    MAX_VERIFY_CODE_TRY("forbidden", "Maximum times of attempts to security verify has been reached in 24 hours", 403),
    USER_NOT_FOUND("not_found", "No user found in the system database", 404),
    HUMAN_VERIFY_ERROR("forbidden", "Human-machine verification failed", 403),
    USERNAME_INVALID("invalid_request", "Username is invalid", 400),
    PASSWORD_INVALID("invalid_request", "Password is invalid", 400),
    OPERATION_FREQUENT("service_unavailable", "User actions are too frequent", 503),
    DB_REDIS_ERROR("insufficient_storage", "Redis database operation error", 507),
    DB_MYSQL_ERROR("insufficient_storage", "MySQL database operation error", 507),
    NW_MAIL_ERROR("internal_server_error", "Send email error", 500),
}
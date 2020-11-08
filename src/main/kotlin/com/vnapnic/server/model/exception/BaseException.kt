package com.vnapnic.server.model.exception

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.ZonedDateTime

@JsonIgnoreProperties("stackTrace", "message", "suppressed", "localizedMessage")
class BaseException(val error: String, val meaning: String, val status: Int,
                    val timestamp: ZonedDateTime = ZonedDateTime.now()
) : RuntimeException("{" +
        "\"errorCode\": $\"$error\"," +
        "\"status\": $\"$status\"," +
        "\"message\": $\"$meaning\"," +
        "\"timestamp\": $\"$timestamp\"" +
        "}") {
    constructor(error: ExceptionEnum) : this(error.error, error.message, error.status)
}
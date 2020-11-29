package com.vnapnic.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
class InvalidTokenRequestException constructor(private val tokenType: String, private val token: String, private val msg: String) :
        RuntimeException(String.format("%s: [%s] token: [%s] ", msg, tokenType, token)) {
}
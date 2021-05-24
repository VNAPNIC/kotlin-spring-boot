package com.vnapnic.database.exception

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException

class UserNotFound(statusText: String)
    : HttpClientErrorException(HttpStatus.NOT_FOUND, statusText)
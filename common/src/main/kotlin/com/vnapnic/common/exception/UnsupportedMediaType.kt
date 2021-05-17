package com.vnapnic.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException

class UnsupportedMediaType(statusText: String) : HttpClientErrorException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, statusText)
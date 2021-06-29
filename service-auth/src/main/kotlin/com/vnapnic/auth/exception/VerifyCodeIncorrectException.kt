package com.vnapnic.auth.exception

import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice
class VerifyCodeIncorrectException(val incorrectCount: Int? = 3) : Exception()
package com.vnapnic.auth.exception

import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice
class VerifyCodeNotCorrectException : Exception()
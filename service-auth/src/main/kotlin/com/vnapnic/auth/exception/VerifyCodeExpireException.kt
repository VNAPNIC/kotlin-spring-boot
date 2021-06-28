package com.vnapnic.auth.exception

import org.springframework.web.bind.annotation.ControllerAdvice
import java.lang.Exception

@ControllerAdvice
class VerifyCodeExpireException : Exception()
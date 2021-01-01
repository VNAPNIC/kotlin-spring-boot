package com.vnapnic.common.exception

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class GlobalExceptionHandler @Autowired constructor(var messageSource: MessageSource) {

    @ResponseBody
    @ExceptionHandler(value = ApiException::class.java)
    fun Response handle()
}
package com.vnapnic.user.model.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletResponse

@RestControllerAdvice
class GlobalExceptionHandler {
    object ExceptionLogger {
        val log: Logger
            get() = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    @ExceptionHandler(value = [BaseException::class])
    fun exceptionHandler(exception: BaseException, response: HttpServletResponse):BaseException {
        ExceptionLogger.log.error(exception.toString())
        response.status = exception.status
        return exception
    }
}
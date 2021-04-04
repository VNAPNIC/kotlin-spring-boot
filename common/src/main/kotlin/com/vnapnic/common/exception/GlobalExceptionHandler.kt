package com.vnapnic.common.exception

import com.vnapnic.common.models.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

/**
 * Global exception handling
 */
@ControllerAdvice
class GlobalExceptionHandler @Autowired constructor(var messageSource: MessageSource) {

    @ResponseBody
    @ExceptionHandler(value = [ApiException::class])
    fun <T> handle(e: ApiException): Response<T> = Response.failed(errorCode = e.errorCode, message = e.message)

    @ResponseBody
    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun <T> handleValidException(e: MethodArgumentNotValidException): Response<T> {
        val bindingResult = e.bindingResult
        val message = if (bindingResult.hasErrors()) {
            val fieldError = bindingResult.fieldError
            if (fieldError != null)
                fieldError.field + fieldError.defaultMessage
            else
                null
        } else null
        return Response.validateFailed(message)
    }

    @ResponseBody
    @ExceptionHandler(value = [BindException::class])
    fun <T> handleValidException(e: BindException): Response<T> {
        val bindingResult = e.bindingResult
        val message = if (bindingResult.hasErrors()) {
            val fieldError = bindingResult.fieldError
            if (fieldError != null)
                fieldError.field + fieldError.defaultMessage
            else
                null
        } else null
        return Response.validateFailed(message)
    }
}
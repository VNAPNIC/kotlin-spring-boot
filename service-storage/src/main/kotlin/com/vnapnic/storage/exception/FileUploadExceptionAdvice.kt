package com.vnapnic.storage.exception

import com.vnapnic.common.beans.ErrorCode
import com.vnapnic.common.beans.Response
import com.vnapnic.common.beans.ResultCode
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class FileUploadExceptionAdvice : ResponseEntityExceptionHandler() {
    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxSizeException(exc: MaxUploadSizeExceededException?): Response {
        return Response.failed(ResultCode.EXPECTATION_FAILED, ErrorCode.FILE_TOO_LARGE)
    }
}
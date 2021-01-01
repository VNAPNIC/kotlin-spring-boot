package com.vnapnic.common.models

import kotlinx.coroutines.*

class Response<T> {
    private var code: Long = 0
    private var message: String? = null
    private var data: T? = null

    constructor(code:Long,message:String?,data: T?){
        this@Response.code = code
        this@Response.message = message
        this@Response.data = data
    }

    fun <T> success(data: T): Response<T>? {
        return Response(ResultCode.SUCCESS.code, ResultCode.SUCCESS.message, data)
    }

    fun <T> success(data: T, message: String?): Response<T>? {
        return Response(ResultCode.SUCCESS.code, message, data)
    }

    fun <T> failed(errorCode: IErrorCode): Response<T>? {
        return Response(errorCode.getCode(), errorCode.getMessage(), null)
    }

    fun <T> failed(errorCode: IErrorCode, message: String?): Response<T>? {
        return Response(errorCode.getCode(), message, null)
    }

    fun <T> failed(message: String?): Response<T>? {
        corout  =ươineScope{

        }
        return Response(ResultCode.FAILED.code, message, null)
    }

    fun <T> failed(): Response<T>? {
        return failed(ResultCode.FAILED)
    }

    fun <T> validateFailed(): Response<T>? {
        return failed(ResultCode.VALIDATE_FAILED)
    }

    fun <T> validateFailed(message: String?): Response<T>? {
        return Response(ResultCode.VALIDATE_FAILED.code, message, null)
    }

    fun <T> unauthorized(data: T): Response<T>? {
        return Response(ResultCode.UNAUTHORIZED.code, ResultCode.UNAUTHORIZED.message, data)
    }

    fun <T> forbidden(data: T): Response<T>? {
        return Response(ResultCode.FORBIDDEN.code, ResultCode.FORBIDDEN.message, data)
    }
}
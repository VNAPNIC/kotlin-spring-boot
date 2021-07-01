package com.vnapnic.common.entities

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 *
 * The response successfully format
 * {
 *  "timestamp": "2021-06-28T06:10:11.786+00:00",
 *  "code": 200,
 *  "message": "",
 *  "error": "",
 *  "token": "",
 *  "refreshToken": "",
 *  "data": Any
 * }
 *
 * The response failed format
 * {
 *  "timestamp": "2021-06-28T06:10:11.786+00:00",
 *  "code": 200,
 *  "message": "",
 *  "error": "",
 *  "errorBody": Any
 * }
 *
 */
class Response<T>(
        private val timestamp: String = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now()),
        val code: Int = ResultCode.SUCCESS.code,
        val message: String? = ResultCode.SUCCESS.message,
        val status: HttpStatus = HttpStatus.OK,
        val error: String? = "",
        val token: String? = null,
        val refreshToken: String? = null,
        val data: T? = null
) : ResponseEntity<Any>(status) {

    data class CustomResponseBody(
            val timestamp: String = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
                    .withZone(ZoneOffset.UTC)
                    .format(Instant.now()),
            val code: Int = ResultCode.SUCCESS.code,
            val message: String? = ResultCode.SUCCESS.message,
            val error: String? = "",
            val token: String? = null,
            val refreshToken: String? = null,
            val data: Any? = null,
    )

    data class CustomErrorBody(
            val timestamp: String = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
                    .withZone(ZoneOffset.UTC)
                    .format(Instant.now()),
            val code: Int = ResultCode.SUCCESS.code,
            val message: String? = ResultCode.SUCCESS.message,
            val error: String? = "",
            val errorBody: Any? = null,
    )

    override fun getBody(): Any? = if (code == ResultCode.SUCCESS.code)
        CustomResponseBody(timestamp, code, message, error, token, refreshToken, data)
    else
        CustomErrorBody(timestamp, code, message, error, data)

    companion object {
        /**
         * Successfully returned results
         *
         * @param data the data obtained
         * @param message prompt message
         */
        fun <T> success(data: T? = null, message: String = ResultCode.SUCCESS.message): Response<T> {
            return Response(message = message, data = data)
        }

        fun <T> success(data: T? = null, token: String? = null, message: String = ResultCode.SUCCESS.message): Response<T> {
            return Response(message = message, token = token, data = data)
        }

        fun <T> success(data: List<T>? = null, token: String? = null, message: String = ResultCode.SUCCESS.message): Response<List<T>> {
            return Response(message = message, token = token, data = data)
        }

        fun success(token: String? = null, message: String = ResultCode.SUCCESS.message): Response<*> {
            return Response(message = message, token = token, data = null)
        }

        fun <T> failedWithData(error: ResultCode = ResultCode.FAILED, errorBody: T?): Response<*> {
            return Response(code = error.code,
                    message = error.message,
                    status = HttpStatus.PRECONDITION_FAILED,
                    error = error.message,
                    data = errorBody)
        }

        /**
         * Failed to return result
         * @param error error [ResultCode]
         */
        fun failed(error: ResultCode = ResultCode.FAILED): Response<*> {
            return Response(
                    code = error.code,
                    message = error.message,
                    status = HttpStatus.PRECONDITION_FAILED,
                    error = error.message,
                    data = null
            )
        }

        fun badRequest(error: ResultCode = ResultCode.WARNING_DATA_FORMAT): Response<*> {
            return Response(
                    code = error.code,
                    message = error.message,
                    status = HttpStatus.BAD_REQUEST,
                    error = error.message,
                    data = null
            )
        }

        /**
         * Not login to return results
         */
        fun unauthorized(data: Any? = null): Response<*> {
            return Response(
                    code = ResultCode.UNAUTHORIZED.code,
                    message = ResultCode.UNAUTHORIZED.message,
                    status = HttpStatus.UNAUTHORIZED,
                    error = ResultCode.UNAUTHORIZED.message,
                    data = data
            )
        }

        /**
         * Not login to return results
         */
        fun unauthorized(data: Any? = null, message: String? = null): Response<*> {
            return Response(
                    code = ResultCode.UNAUTHORIZED.code,
                    message = message,
                    status = HttpStatus.UNAUTHORIZED,
                    error = message,
                    data = data
            )
        }

        /**
         * Not authorized to return results
         */
        fun forbidden(data: Any? = null): Response<*>? {
            return Response(
                    code = ResultCode.FORBIDDEN.code,
                    message = ResultCode.FORBIDDEN.message,
                    status = HttpStatus.FORBIDDEN,
                    error = ResultCode.FORBIDDEN.message,
                    data = data
            )
        }
    }
}
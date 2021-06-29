package com.vnapnic.common.entities

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * the response format
 * {
 *  "timestamp": "2021-06-28T06:10:11.786+00:00",
 *  "code": 200,
 *  "message": "",
 *  "error": "",
 *  "token": "",
 *  "data": Any
 * }
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
        val data: T? = null,
        private val errorBody: T? = null
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
            val token: String? = null,
            val errorBody: Any? = null,
    )

    override fun getBody(): Any? = if (code == ResultCode.SUCCESS.code) CustomResponseBody(timestamp, code, message, error, token, data) else CustomErrorBody(timestamp, code, message, error, token, errorBody)

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
                    status = HttpStatus.OK,
                    error = error.message,
                    errorBody = errorBody)
        }

        /**
         * Failed to return result
         * @param error error [ResultCode]
         */
        fun failed(error: ResultCode = ResultCode.FAILED): Response<*> {
            return Response(
                    code = error.code,
                    message = error.message,
                    status = HttpStatus.INTERNAL_SERVER_ERROR,
                    error = error.message,
                    errorBody = null
            )
        }

        fun badRequest(error: ResultCode = ResultCode.WARNING_DATA_FORMAT): Response<*> {
            return Response(
                    code = error.code,
                    message = error.message,
                    status = HttpStatus.BAD_REQUEST,
                    error = error.message,
                    errorBody = null
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
                    errorBody = data
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
                    errorBody = data
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
                    errorBody = data
            )
        }
    }
}
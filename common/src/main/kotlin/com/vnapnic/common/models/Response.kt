package com.vnapnic.common.models

class Response<T>(val code: Long, val message: String?, val errorCode: Long? = null, val error: String? = null, val token: String? = null, val data: T? = null) {

    companion object {
        /**
         * Successfully returned results
         *
         * @param data the data obtained
         * @param message prompt message
         */
        fun <T> success(data: T? = null, message: String = ResultCode.SUCCESS.message): Response<T> {
            return Response(code = ResultCode.SUCCESS.code, message = message, data = data)
        }

        fun <T> success(data: T? = null, token: String? = null, message: String = ResultCode.SUCCESS.message): Response<T> {
            return Response(code = ResultCode.SUCCESS.code, message = message, token = token, data = data)
        }

        /**
         * Failed to return result
         * @param status error [ResultCode]
         * @param error error [ErrorCode]
         */
        fun <T> failed(status: ResultCode? = null, error: ErrorCode): Response<T> {
            return Response(code = status?.code
                    ?: ResultCode.FAILED.code, message = status?.message, errorCode = error.code, error = error.message, data = null)
        }

        /**
         * Parameter verification failed and return result
         * @param message prompt message
         */
        fun <T> validateFailed(message: String?): Response<T> {
            return Response(code = ResultCode.VALIDATE_FAILED.code, message = message, data = null);
        }

        /**
         * Not login to return results
         */
        fun <T> unauthorized(data: T? = null): Response<T> {
            return Response(code = ResultCode.UNAUTHORIZED.code, message = ResultCode.UNAUTHORIZED.message, data = null)
        }

        /**
         * Not authorized to return results
         */
        fun <T> forbidden(data: T? = null): Response<T>? {
            return Response(code = ResultCode.FORBIDDEN.code, message = ResultCode.FORBIDDEN.message, data = data)
        }
    }
}
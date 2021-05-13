package com.vnapnic.common.models

class Response<T>(val code: Long, val message: String?, val token: String? = null, val data: T? = null) {

    companion object {
        /**
         * Successfully returned results
         *
         * @param data the data obtained
         * @param message prompt message
         */
        fun <T> success(data: T? = null, message: String = ResultCode.SUCCESS.message): Response<T> {
            return Response(ResultCode.SUCCESS.code, message, data = data)
        }

        fun <T> success(data: T? = null, token: String? = null, message: String = ResultCode.SUCCESS.message): Response<T> {
            return Response(ResultCode.SUCCESS.code, message, token, data)
        }

        /**
         * Failed to return result
         * @param errorCode error code
         * @param message error message
         */
        fun <T> failed(errorCode: IErrorCode? = null, message: String? = null): Response<T> {
            return Response(errorCode?.getCode() ?: ResultCode.FAILED.code, message, null)
        }

        /**
         * Parameter verification failed and return result
         * @param message prompt message
         */
        fun <T> validateFailed(message: String?): Response<T> {
            return Response(ResultCode.VALIDATE_FAILED.code, message, null);
        }

        /**
         * Not login to return results
         */
        fun <T> unauthorized(data: T? = null): Response<T> {
            return Response(ResultCode.UNAUTHORIZED.code, ResultCode.UNAUTHORIZED.message, data = null)
        }

        /**
         * Not authorized to return results
         */
        fun <T> forbidden(data: T? = null): Response<T>? {
            return Response(ResultCode.FORBIDDEN.code, ResultCode.FORBIDDEN.message, data = data)
        }
    }
}
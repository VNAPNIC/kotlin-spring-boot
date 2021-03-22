package com.vnapnic.common.models

class Response<T>(val code: Long, val message: String?, val data: T?) {

    companion object {
        /**
         * Successfully returned results
         *
         * @param data the data obtained
         * @param message prompt message
         */
        fun <T> success(data: T, message: String = ResultCode.SUCCESS.message): Response<T> {
            return Response(ResultCode.SUCCESS.code, message, data)
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
        fun <T> unauthorized(data: T): Response<T> {
            return Response(ResultCode.UNAUTHORIZED.code, ResultCode.UNAUTHORIZED.message, data)
        }

        /**
         * Not authorized to return results
         */
        fun <T> forbidden(data: T): Response<T>? {
            return Response(ResultCode.FORBIDDEN.code, ResultCode.FORBIDDEN.message, data)
        }
    }
}
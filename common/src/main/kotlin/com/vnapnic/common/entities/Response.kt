package com.vnapnic.common.entities

class Response<T>(val code: Long = ResultCode.SUCCESS.code,
                  val message: String? = ResultCode.SUCCESS.message,
                  val errorCode: Long? = null,
                  val error: String? = null,
                  val token: String? = null,
                  val data: T? = null
) {

    companion object {
        /**
         * Successfully returned results
         *
         * @param data the data obtained
         * @param message prompt message
         */
        fun <T>success(data: T? = null, message: String = ResultCode.SUCCESS.message): Response<T> {
            return Response(code = ResultCode.SUCCESS.code, message = message, data = data)
        }

        fun <T>success(data: T? = null, token: String? = null, message: String = ResultCode.SUCCESS.message): Response<T> {
            return Response(code = ResultCode.SUCCESS.code, message = message, token = token, data = data)
        }

        fun <T>success(data: List<T>? = null, token: String? = null, message: String = ResultCode.SUCCESS.message): Response<List<T>> {
            return Response(code = ResultCode.SUCCESS.code, message = message, token = token, data = data)
        }

        fun success(token: String? = null, message: String = ResultCode.SUCCESS.message): Response<*> {
            return Response(code = ResultCode.SUCCESS.code, message = message, token = token, data = null)
        }

        /**
         * Failed to return result
         * @param status error [ResultCode]
         * @param error error [ErrorCode]
         */
        fun failed(status: ResultCode? = null, error: ResultCode): Response<*> {
            return Response(code = status?.code
                    ?: ResultCode.FAILED.code, message = status?.message, errorCode = error.code, error = error.message, data = null)
        }

        /**
         * Parameter verification failed and return result
         * @param message prompt message
         */
        fun validateFailed(message: String?): Response<*> {
            return Response(code = ResultCode.VALIDATE_FAILED.code, message = message, data = null);
        }

        /**
         * Not login to return results
         */
        fun unauthorized(data: Any? = null): Response<*> {
            return Response(code = ResultCode.UNAUTHORIZED.code, message = ResultCode.UNAUTHORIZED.message, data = data)
        }

        /**
         * Not login to return results
         */
        fun unauthorized(data: Any? = null, message: String? = null): Response<*> {
            return Response(code = ResultCode.UNAUTHORIZED.code, message = message
                    ?: ResultCode.UNAUTHORIZED.message, data = data)
        }

        /**
         * Not authorized to return results
         */
        fun forbidden(data: Any? = null): Response<*>? {
            return Response(code = ResultCode.FORBIDDEN.code, message = ResultCode.FORBIDDEN.message, data = data)
        }
    }
}
package com.vnapnic.common.exception

import com.vnapnic.common.models.IErrorCode

class ApiException : RuntimeException {
    var errorCode: IErrorCode? = null

    constructor(errorCode: IErrorCode) : super(errorCode.getMessage()) {
        this.errorCode = errorCode
    }

    constructor(message: String?) : super(message) {

    }

    constructor(cause: Throwable?) : super(cause) {

    }

    constructor(message: String?, cause: Throwable?) : super(message, cause) {

    }
}
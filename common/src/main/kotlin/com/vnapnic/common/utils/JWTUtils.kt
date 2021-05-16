package com.vnapnic.common.utils

object JWTUtils {
    fun tokenFromBearerToken(authorization: String?): String? = authorization?.substring(7, authorization.length)
}
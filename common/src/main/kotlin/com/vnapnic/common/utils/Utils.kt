package com.vnapnic.common.utils

inline fun <reified T : Enum<T>> valueOf(type: String): T? {
    return try {
        java.lang.Enum.valueOf(T::class.java, type)
    } catch (e: Exception) {
        null
    }
}
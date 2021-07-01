package com.vnapnic.common.exception

fun String.toToken(): String? = substring(7, length)
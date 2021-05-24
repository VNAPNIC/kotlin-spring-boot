package com.vnapnic.common.utils

fun String.isPhoneNumber() : Boolean {
    return "^[+]?[0-9]{9,13}\$".toRegex().matches(this)
}

fun String.isEmail() : Boolean {
    return "^[A-Za-z](.*)([@])(.+)(\\.)(.+)".toRegex().matches(this)
}
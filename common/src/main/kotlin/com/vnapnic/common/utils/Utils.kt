package com.vnapnic.common.utils

fun String.isEmail() : Boolean {
    return "^[A-Za-z](.*)([@])(.+)(\\.)(.+)".toRegex().matches(this)
}
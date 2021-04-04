package com.vnapnic.common.models

interface IErrorCode {
    fun getCode(): Long
    fun getMessage(): String
}
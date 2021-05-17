package com.vnapnic.common.db

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Gender(val value:Int) {
    WOMEN(0),
    MAN(1),
    OTHER(3)
}
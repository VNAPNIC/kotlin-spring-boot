package com.vnapnic.user.repositories

import org.springframework.stereotype.Repository

@Repository
class DemoRepository {
    fun test(name: String): String = "testttttttttt: $name"
}
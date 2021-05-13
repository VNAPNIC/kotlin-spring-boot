package com.vnapnic.auth.repositories

//@Repository
interface AccountRepository {
    fun findByUsername(username: String?): Account?
    fun findByEmail(email: String?): Account?
    fun findBySocialId(socialId: String?): Account?
}
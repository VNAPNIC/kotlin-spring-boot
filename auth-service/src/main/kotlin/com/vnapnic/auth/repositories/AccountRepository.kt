package com.vnapnic.auth.repositories

import com.vnapnic.auth.model.Account
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : MongoRepository<Account, String>{
    fun findByEmail(email: String?): Account?
    fun findBySocialId(socialId: String?): Account?
    fun findByPhoneNumber(phoneNumber: String?): Account?

    fun existsBySocialId(socialId: String?): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
}
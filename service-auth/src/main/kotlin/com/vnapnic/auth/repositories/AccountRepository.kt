package com.vnapnic.auth.repositories

import com.vnapnic.database.beans.AccountBean
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : MongoRepository<AccountBean, String> {
    fun findByEmail(email: String?): AccountBean?
    fun findBySocialId(socialId: String?): AccountBean?
    fun findByPhoneNumber(phoneNumber: String?): AccountBean?

    fun existsBySocialId(socialId: String?): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
}
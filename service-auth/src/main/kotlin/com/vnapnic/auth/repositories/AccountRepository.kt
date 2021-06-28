package com.vnapnic.auth.repositories

import com.vnapnic.database.entities.AccountEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : MongoRepository<AccountEntity, String> {
    fun findByEmail(email: String?): AccountEntity?
    fun findBySocialId(socialId: String?): AccountEntity?
    fun findByPhoneNumber(phoneNumber: String?): AccountEntity?

    fun existsBySocialId(socialId: String?): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
}
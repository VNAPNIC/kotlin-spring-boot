package com.vnapnic.auth.repositories

import com.vnapnic.auth.model.Account
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : MongoRepository<Account, String>{
    fun findByUsername(username: String?): Account?
    fun findByEmail(email: String?): Account?
    fun findBySocialId(socialId: String?): Account?
}
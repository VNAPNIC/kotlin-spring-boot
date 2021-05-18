package com.vnapnic.user.repositories

import com.vnapnic.common.db.Account
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<Account, String> {

}
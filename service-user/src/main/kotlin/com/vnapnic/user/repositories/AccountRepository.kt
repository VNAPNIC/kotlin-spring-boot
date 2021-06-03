package com.vnapnic.user.repositories

import com.vnapnic.database.entities.AccountEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : MongoRepository<AccountEntity, String>
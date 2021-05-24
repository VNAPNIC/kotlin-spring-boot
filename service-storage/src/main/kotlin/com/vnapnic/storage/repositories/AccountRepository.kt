package com.vnapnic.storage.repositories

import com.vnapnic.database.beans.AccountBean
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : MongoRepository<AccountBean, String>
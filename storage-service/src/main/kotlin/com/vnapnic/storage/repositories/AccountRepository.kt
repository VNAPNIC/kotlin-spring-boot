package com.vnapnic.storage.repositories

import com.vnapnic.common.db.Account
import com.vnapnic.common.db.Device
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : MongoRepository<Account, String>
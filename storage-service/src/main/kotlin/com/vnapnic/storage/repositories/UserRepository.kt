package com.vnapnic.storage.repositories

import com.vnapnic.common.db.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, String>
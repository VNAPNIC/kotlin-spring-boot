package com.vnapnic.auth.repositories

import com.vnapnic.database.entities.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<UserEntity, String>
package com.vnapnic.auth.repositories

import com.vnapnic.database.entities.LoginHistoryEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface LoginHistoryRepository : MongoRepository<LoginHistoryEntity, String>
package com.vnapnic.auth.repositories

import com.vnapnic.database.beans.LoginHistoryBean
import org.springframework.data.mongodb.repository.MongoRepository

interface LoginHistoryRepository : MongoRepository<LoginHistoryBean, String>
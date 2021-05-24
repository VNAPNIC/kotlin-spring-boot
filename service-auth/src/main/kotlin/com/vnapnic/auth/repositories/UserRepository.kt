package com.vnapnic.auth.repositories

import com.vnapnic.database.beans.UserBean
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<UserBean, String>
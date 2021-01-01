package com.vnapnic.user.repositories

import com.vnapnic.user.models.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long>
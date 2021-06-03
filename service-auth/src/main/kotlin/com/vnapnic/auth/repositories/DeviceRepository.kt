package com.vnapnic.auth.repositories

import com.vnapnic.database.entities.DeviceEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface DeviceRepository : MongoRepository<DeviceEntity, String>
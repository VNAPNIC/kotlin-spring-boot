package com.vnapnic.auth.repositories

import com.vnapnic.database.beans.DeviceBean
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface DeviceRepository : MongoRepository<DeviceBean, String>
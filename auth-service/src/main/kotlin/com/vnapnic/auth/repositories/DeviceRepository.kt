package com.vnapnic.auth.repositories

import com.vnapnic.common.db.Device
import com.vnapnic.common.db.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface DeviceRepository : MongoRepository<Device, String>
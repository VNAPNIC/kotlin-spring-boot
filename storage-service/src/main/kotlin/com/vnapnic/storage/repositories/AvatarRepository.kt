package com.vnapnic.storage.repositories

import com.vnapnic.common.db.files.AvatarInfo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AvatarRepository : MongoRepository<AvatarInfo, String>
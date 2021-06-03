package com.vnapnic.storage.repositories

import com.vnapnic.database.entities.files.AvatarInfoEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AvatarRepository : MongoRepository<AvatarInfoEntity, String>{
    fun findByFileName(keyName: String) : AvatarInfoEntity?
}
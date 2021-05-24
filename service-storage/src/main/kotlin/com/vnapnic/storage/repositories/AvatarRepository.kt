package com.vnapnic.storage.repositories

import com.vnapnic.database.beans.files.AvatarInfoBean
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AvatarRepository : MongoRepository<AvatarInfoBean, String>{
    fun findByFileName(keyName: String) : AvatarInfoBean?
}
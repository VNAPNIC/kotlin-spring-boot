package com.vnapnic.storage.repositories

import com.vnapnic.database.entities.files.FileInfoEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FilesRepository : MongoRepository<FileInfoEntity, String>{
    fun findByFileName(keyName: String) : FileInfoEntity?
}
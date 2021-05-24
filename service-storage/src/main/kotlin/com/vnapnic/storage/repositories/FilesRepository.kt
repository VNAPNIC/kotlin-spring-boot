package com.vnapnic.storage.repositories

import com.vnapnic.database.beans.files.FileInfoBean
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FilesRepository : MongoRepository<FileInfoBean, String>{
    fun findByFileName(keyName: String) : FileInfoBean?
}
package com.vnapnic.storage.repositories

import com.vnapnic.common.db.files.FileInfo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FilesRepository : MongoRepository<FileInfo, String>
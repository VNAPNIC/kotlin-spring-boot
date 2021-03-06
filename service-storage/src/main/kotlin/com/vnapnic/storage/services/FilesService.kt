package com.vnapnic.storage.services

import com.vnapnic.database.entities.files.AvatarInfoEntity
import com.vnapnic.database.entities.files.FileInfoEntity
import com.vnapnic.database.storage.Storage
import com.vnapnic.database.storage.avatarPath
import com.vnapnic.database.storage.imagePath
import com.vnapnic.database.storage.videoPath
import com.vnapnic.storage.repositories.AvatarRepository
import com.vnapnic.storage.repositories.FilesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

interface FilesService {
    @Throws(Exception::class)
    fun avatarFindByKeyName(key: String): AvatarInfoEntity?

    @Throws(Exception::class)
    fun fileFindByKey(key: String): FileInfoEntity?

    @Throws(Exception::class)
    fun avatarFetch(key: String): Resource?

    @Throws(Exception::class)
    fun imageFetch(key: String): Resource?

    @Throws(Exception::class)
    fun videoFetch(key: String): Resource?

    @Throws(Exception::class)
    fun download(key: String)
}

@Service
class FilesServiceImpl : FilesService {

    @Autowired
    lateinit var filesRepository: FilesRepository

    @Autowired
    lateinit var avatarRepository: AvatarRepository

    @Autowired
    lateinit var localStorage: Storage

    @Throws(Exception::class)
    override fun avatarFindByKeyName(key: String): AvatarInfoEntity? = avatarRepository.findByFileName(key)

    @Throws(Exception::class)
    override fun fileFindByKey(key: String): FileInfoEntity? = filesRepository.findByFileName(key)

    @Throws(Exception::class)
    override fun avatarFetch(key: String): Resource? {
        return localStorage.loadAsResource(avatarPath, key)
    }

    @Throws(Exception::class)
    override fun imageFetch(key: String): Resource? {
        return localStorage.loadAsResource(imagePath, key)
    }

    @Throws(Exception::class)
    override fun videoFetch(key: String): Resource? {
        return localStorage.loadAsResource(videoPath, key)
    }

    override fun download(key: String) {
    }
}
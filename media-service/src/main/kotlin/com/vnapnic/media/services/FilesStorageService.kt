package com.vnapnic.media.services

import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.stream.Stream


interface FilesStorageService {
    fun init()

    @Throws(Exception::class)
    fun saveAvatar(file: MultipartFile)

    @Throws(Exception::class)
    fun saveImage(file: MultipartFile)

    @Throws(Exception::class)
    fun saveVideo(file: MultipartFile)

    fun load(filename: String?): Resource?

    fun deleteAll()

    fun loadAll(): Stream<Path?>?
}

@Service
class FilesStorageServiceImpl : FilesStorageService {

    private val log = LoggerFactory.getLogger(FilesStorageServiceImpl::class.java)

    private val avatarPath: Path = Paths.get("uploads/avatars")
    private val imagePath: Path = Paths.get("uploads/images")
    private val videoPath: Path = Paths.get("uploads/videos")

    override fun init() {
        try {
            if (Files.notExists(avatarPath)) {
                Files.createDirectories(avatarPath)
                log.info(avatarPath.toAbsolutePath().toUri().toString())
            }
            if (Files.notExists(imagePath)) {
                Files.createDirectories(imagePath)
                log.info(imagePath.toAbsolutePath().toUri().toString())
            }
            if (Files.notExists(videoPath)) {
                Files.createDirectories(videoPath)
                log.info(videoPath.toAbsolutePath().toUri().toString())
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw RuntimeException("Could not initialize folder for upload!")
        }
    }

    @Throws(Exception::class)
    override fun saveAvatar(file: MultipartFile) {
        Files.copy(file.inputStream, avatarPath.resolve(file.originalFilename), StandardCopyOption.REPLACE_EXISTING)
    }

    @Throws(Exception::class)
    override fun saveImage(file: MultipartFile) {
        Files.copy(file.inputStream, imagePath.resolve(file.originalFilename), StandardCopyOption.REPLACE_EXISTING)
    }

    @Throws(Exception::class)
    override fun saveVideo(file: MultipartFile) {
        Files.copy(file.inputStream, videoPath.resolve(file.originalFilename), StandardCopyOption.REPLACE_EXISTING)
    }

    override fun load(filename: String?): Resource? {

        return null
    }

    override fun deleteAll() {
    }

    override fun loadAll(): Stream<Path?>? {

        return null
    }
}
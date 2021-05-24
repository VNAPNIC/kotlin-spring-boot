package com.vnapnic.storage.services

import com.vnapnic.database.beans.UserBean
import com.vnapnic.database.beans.files.AvatarInfoBean
import com.vnapnic.database.beans.files.FileInfoBean
import com.vnapnic.database.exception.UnsupportedMediaType
import com.vnapnic.database.storage.*
import com.vnapnic.storage.dto.FileDTO
import com.vnapnic.storage.repositories.AccountRepository
import com.vnapnic.storage.repositories.AvatarRepository
import com.vnapnic.storage.repositories.FilesRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.util.*
import kotlin.jvm.Throws

interface FilesStorageService {
    fun init()

    @Throws(Exception::class)
    fun saveAvatarToUser(avatarInfo: AvatarInfoBean)

    @Throws(Exception::class)
    fun saveAvatar(accountId: String?, deviceId: String?, multipartFile: MultipartFile): FileDTO

    @Throws(Exception::class)
    fun saveFiles(accountId: String?, deviceId: String?, multipartFile: MultipartFile): FileDTO

    @Throws(Exception::class)
    fun saveImage(accountId: String?, deviceId: String?, multipartFile: MultipartFile): FileDTO

    @Throws(Exception::class)
    fun saveVideo(accountId: String?, deviceId: String?, multipartFile: MultipartFile): FileDTO
}

@Service
class FilesStorageServiceImpl : FilesStorageService {

    private val log = LoggerFactory.getLogger(FilesStorageServiceImpl::class.java)

    @Autowired
    lateinit var avatarRepository: AvatarRepository

    @Autowired
    lateinit var filesRepository: FilesRepository

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var mongoOperations: MongoOperations

    @Autowired
    lateinit var localStorage: Storage

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
    override fun saveAvatarToUser(avatarInfo: AvatarInfoBean) {
        val account = avatarInfo.recorder?.let { accountId -> accountRepository.findById(accountId) }
                ?: throw Exception("can't save avatar to user")
        val user = account.get().info
        val query = Query(Criteria.where("_id").`is`(user?.id))
        val update = Update().set("avatar", avatarInfo)
        mongoOperations.updateFirst(query, update, UserBean::class.java)
    }

    @Throws(Exception::class)
    override fun saveAvatar(accountId: String?, deviceId: String?, multipartFile: MultipartFile): FileDTO {
        val file = localStorage.storeImage(avatarPath, multipartFile.inputStream, multipartFile.size, multipartFile.originalFilename)
        val avatarInfo = AvatarInfoBean()
        avatarInfo.recorder = accountId
        avatarInfo.deviceId = deviceId
        avatarInfo.recordDate = Date(file.name.split(".")[0].toLong())
        avatarInfo.path = "$AVATAR_URL\\${file.name}"
        avatarInfo.fileName = file.name
        avatarInfo.fileExtName = ImageUtils.getFileExtension(file)
        avatarInfo.contentType = Files.probeContentType(file.toPath())
        val result = avatarRepository.save(avatarInfo)
        saveAvatarToUser(result)
        return FileDTO(
                fileId = result.id,
                fileName = result.fileName,
                fileExtName = result.fileExtName,
                contentType = result.contentType
        )
    }

    @Throws(Exception::class)
    override fun saveFiles(accountId: String?, deviceId: String?, multipartFile: MultipartFile): FileDTO {
        if (multipartFile.originalFilename == null)
            throw UnsupportedMediaType("Upload file fail.")

        return if (ImageUtils.getFileExtension(multipartFile.originalFilename)?.toLowerCase() == "png"
                || ImageUtils.getFileExtension(multipartFile.originalFilename)?.toLowerCase() == "jpg"
                || ImageUtils.getFileExtension(multipartFile.originalFilename)?.toLowerCase() == "jpeg")
            saveImage(accountId, deviceId, multipartFile)
        else if (ImageUtils.getFileExtension(multipartFile.originalFilename)?.toLowerCase() == "mp4"
                || ImageUtils.getFileExtension(multipartFile.originalFilename)?.toLowerCase() == "avi"
                || ImageUtils.getFileExtension(multipartFile.originalFilename)?.toLowerCase() == "m4v"
                || ImageUtils.getFileExtension(multipartFile.originalFilename)?.toLowerCase() == "mov")
            saveVideo(accountId, deviceId, multipartFile)
        else throw UnsupportedMediaType("Upload file fail.")
    }

    @Throws(Exception::class)
    override fun saveImage(accountId: String?, deviceId: String?, multipartFile: MultipartFile): FileDTO {
        val file = localStorage.storeImage(imagePath, multipartFile.inputStream, multipartFile.size, multipartFile.originalFilename)
        val fileInfo = FileInfoBean()
        fileInfo.recorder = accountId
        fileInfo.deviceId = deviceId
        fileInfo.recordDate = Date(file.name.split(".")[0].toLong())
        fileInfo.path = "$IMAGE_URL\\${file.name}"
        fileInfo.fileName = file.name
        fileInfo.fileExtName = ImageUtils.getFileExtension(file)
        fileInfo.contentType = Files.probeContentType(file.toPath())

        val result = filesRepository.save(fileInfo)
        return FileDTO(
                fileId = result.id,
                fileName = result.fileName,
                fileExtName = result.fileExtName,
                contentType = result.contentType
        )
    }

    @Throws(Exception::class)
    override fun saveVideo(accountId: String?, deviceId: String?, multipartFile: MultipartFile): FileDTO {
        val file = localStorage.storeVideo(videoPath, multipartFile.inputStream, multipartFile.size, multipartFile.originalFilename)
        val fileInfo = FileInfoBean()
        fileInfo.recorder = accountId
        fileInfo.deviceId = deviceId
        fileInfo.recordDate = Date(file.name.split(".")[0].toLong())
        fileInfo.path = "$VIDEO_URL\\${file.name}"
        fileInfo.fileName = file.name
        fileInfo.fileExtName = ImageUtils.getFileExtension(file)
        fileInfo.contentType = Files.probeContentType(file.toPath())

        val result = filesRepository.save(fileInfo)
        return FileDTO(
                fileId = result.id,
                fileName = result.fileName,
                fileExtName = result.fileExtName,
                contentType = result.contentType
        )
    }
}
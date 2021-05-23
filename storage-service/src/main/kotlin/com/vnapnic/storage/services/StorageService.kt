package com.vnapnic.storage.services

import com.vnapnic.common.db.User
import com.vnapnic.common.utils.ImageConverter
import com.vnapnic.common.utils.ImageUtils
import com.vnapnic.common.utils.ImageUtils.AVATAR_URL
import com.vnapnic.common.utils.ImageUtils.IMAGE_URL
import com.vnapnic.common.utils.ImageUtils.avatarPath
import com.vnapnic.common.utils.ImageUtils.imagePath
import com.vnapnic.common.utils.ImageUtils.videoPath
import com.vnapnic.common.db.files.AvatarInfo
import com.vnapnic.common.db.files.FileInfo
import com.vnapnic.common.exception.UnsupportedMediaType
import com.vnapnic.storage.repositories.AccountRepository
import com.vnapnic.storage.repositories.AvatarRepository
import com.vnapnic.storage.repositories.FilesRepository
import com.vnapnic.storage.repositories.UserRepository
import org.apache.tomcat.util.http.fileupload.FileUploadException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*
import java.util.stream.Stream

interface FilesStorageService {
    fun init()

    fun saveAvatarToUser(avatarInfo: AvatarInfo)

    @Throws(Exception::class)
    fun saveAvatar(accountId: String?, deviceId: String?, multipartFile: MultipartFile): AvatarInfo

    @Throws(Exception::class)
    fun saveFiles(accountId: String?, deviceId: String?, multipartFile: MultipartFile): FileInfo

    @Throws(Exception::class)
    fun saveImage(accountId: String?, deviceId: String?, multipartFile: MultipartFile): FileInfo

    @Throws(Exception::class)
    fun saveVideo(accountId: String?, deviceId: String?, multipartFile: MultipartFile): FileInfo

    fun load(filename: String?): Resource?

    fun deleteAll()

    fun loadAll(): Stream<Path?>?
}

@Service
class FilesStorageServiceImpl : FilesStorageService {

    private val log = LoggerFactory.getLogger(FilesStorageServiceImpl::class.java)

    @Autowired
    lateinit var avatarRepository: AvatarRepository

    @Autowired
    lateinit var filesRepository: FilesRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var mongoOperations: MongoOperations

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
    override fun saveAvatarToUser(avatarInfo: AvatarInfo) {
        val account = avatarInfo.recorder?.let { accountId -> accountRepository.findById(accountId) }
                ?: throw Exception("can't save avatar to user")
        val user = account.get().info
        val query = Query(Criteria.where("_id").`is`(user?._id))
        val update = Update().set("avatar", avatarInfo)
        mongoOperations.updateFirst(query, update, User::class.java)
    }

    @Throws(Exception::class)
    override fun saveAvatar(accountId: String?, deviceId: String?, multipartFile: MultipartFile): AvatarInfo {
        if (multipartFile.originalFilename == null)
            throw UnsupportedMediaType("Upload file fail.")
        if (ImageUtils.getFileExtension(multipartFile.originalFilename)?.toLowerCase() != "png"
                && ImageUtils.getFileExtension(multipartFile.originalFilename)?.toLowerCase() != "jpg"
                && ImageUtils.getFileExtension(multipartFile.originalFilename)?.toLowerCase() != "jpeg"
        ) throw UnsupportedMediaType("Upload file fail.")

        val result = Files.copy(multipartFile.inputStream, avatarPath.resolve(multipartFile.originalFilename), StandardCopyOption.REPLACE_EXISTING)

        val inputImage = avatarPath.resolve(multipartFile.originalFilename).toAbsolutePath().toString()
        val outputImage = avatarPath.resolve("${System.currentTimeMillis()}.png").toAbsolutePath().toString()
        val formatName = "PNG"

        if (result > 0) {
            val file = ImageConverter.convertType(inputImage = inputImage, outputImage = outputImage, formatName = formatName)
            val avatarInfo = AvatarInfo()
            avatarInfo.recorder = accountId
            avatarInfo.deviceId = deviceId
            avatarInfo.recordDate = Date(file.name.split(".")[0].toLong())
            avatarInfo.path = "$AVATAR_URL\\${file.name}"
            avatarInfo.fileName = file.name
            avatarInfo.fileExtName = ImageUtils.getFileExtension(file)
            avatarInfo.contentType = Files.probeContentType(file.toPath())
            return avatarRepository.save(avatarInfo)
        } else {
            Files.deleteIfExists(Paths.get(outputImage))
            throw FileUploadException("Upload file fail.")
        }
    }

    @Throws(Exception::class)
    override fun saveFiles(accountId: String?, deviceId: String?, multipartFile: MultipartFile): FileInfo {
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
    override fun saveImage(accountId: String?, deviceId: String?, multipartFile: MultipartFile): FileInfo {
        val result = Files.copy(multipartFile.inputStream, imagePath.resolve(multipartFile.originalFilename), StandardCopyOption.REPLACE_EXISTING)
        val inputImage = imagePath.resolve(multipartFile.originalFilename)
        val outputImage = imagePath.resolve("${System.currentTimeMillis()}.png")
        val formatName = "png"
        return if (result > 0) {
            val file = ImageConverter.convertType(inputImage = inputImage.toAbsolutePath().toString(), outputImage = outputImage.toAbsolutePath().toString(), formatName = formatName)
            val fileInfo = FileInfo()
            fileInfo.recorder = accountId
            fileInfo.deviceId = deviceId
            fileInfo.recordDate = Date(file.name.split(".")[0].toLong())
            fileInfo.path = "$IMAGE_URL\\${file.name}"
            fileInfo.fileName = file.name
            fileInfo.fileExtName = ImageUtils.getFileExtension(file)
            fileInfo.contentType = Files.probeContentType(file.toPath())
            filesRepository.save(fileInfo)
        } else {
            Files.deleteIfExists(outputImage)
            throw FileUploadException("Upload file fail.")
        }
    }

    @Throws(Exception::class)
    override fun saveVideo(accountId: String?, deviceId: String?, multipartFile: MultipartFile): FileInfo {
        val result = Files.copy(multipartFile.inputStream, videoPath.resolve(multipartFile.originalFilename), StandardCopyOption.REPLACE_EXISTING)

        val formatName = ImageUtils.getFileExtension(multipartFile.originalFilename)
        val inputImage = videoPath.resolve(multipartFile.originalFilename).toAbsolutePath()
        val outputImage = videoPath.resolve("${System.currentTimeMillis()}.$formatName").toAbsolutePath()
        return if (result > 0) {
            val file = File(Files.move(inputImage, outputImage, StandardCopyOption.REPLACE_EXISTING).toUri())
            val fileInfo = FileInfo()
            fileInfo.recorder = accountId
            fileInfo.deviceId = deviceId
            fileInfo.recordDate = Date(file.name.split(".")[0].toLong())
            fileInfo.path = "$IMAGE_URL\\${file.name}"
            fileInfo.fileName = file.name
            fileInfo.fileExtName = ImageUtils.getFileExtension(file)
            fileInfo.contentType = Files.probeContentType(file.toPath())
            filesRepository.save(fileInfo)
        } else {
            Files.deleteIfExists(outputImage)
            throw FileUploadException("Upload file fail.")
        }
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
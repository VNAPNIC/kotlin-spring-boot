package com.vnapnic.database.storage

import com.vnapnic.database.exception.UnsupportedMediaType
import org.apache.commons.logging.LogFactory
import org.apache.tomcat.util.http.fileupload.FileUploadException
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.stream.Stream

const val AVATAR_URL = "uploads\\avatars"
const val IMAGE_URL = "uploads\\images"
const val VIDEO_URL = "uploads\\videos"

val avatarPath: Path = Paths.get(AVATAR_URL)
val imagePath: Path = Paths.get(IMAGE_URL)
val videoPath: Path = Paths.get(VIDEO_URL)

/**
 * Object storage interface
 */
interface Storage {

    fun storeImage(path: Path, inputStream: InputStream, contentLength: Long, keyName: String?) : File

    fun storeVideo(path: Path, inputStream: InputStream, contentLength: Long, keyName: String?) : File

    fun loadAll(path: Path): Stream<Path?>?
    fun load(path: Path, keyName: String): Path?
    fun loadAsResource(path: Path, keyName: String): Resource?
    fun delete(path: Path, keyName: String)
}

/**
 * Server local object storage service
 */
class LocalStorage : Storage {
    private val logger = LogFactory.getLog(LocalStorage::class.java)

    override fun storeImage(path: Path, inputStream: InputStream, contentLength: Long, keyName: String?) : File {
        if (keyName == null)
            throw UnsupportedMediaType("keyName is null.")
        if (ImageUtils.getFileExtension(keyName)?.toLowerCase() != "png"
                && ImageUtils.getFileExtension(keyName)?.toLowerCase() != "jpg"
                && ImageUtils.getFileExtension(keyName)?.toLowerCase() != "jpeg"
        ) throw UnsupportedMediaType("Unsupported media type.")

        return try {
            val result = Files.copy(inputStream, path.resolve(keyName), StandardCopyOption.REPLACE_EXISTING)

            val inputImage = path.resolve(keyName).toAbsolutePath().toString()
            val outputImage = path.resolve("${System.currentTimeMillis()}.png").toAbsolutePath().toString()
            val formatName = "PNG"

            if(result <= 0){
                Files.deleteIfExists(Paths.get(outputImage))
                throw FileUploadException("Upload file fail.")
            }

            ImageConverter.convertType(inputImage = inputImage, outputImage = outputImage, formatName = formatName)
        } catch (e: IOException) {
            throw RuntimeException("Failed to store file $keyName", e)
        }
    }

    override fun storeVideo(path: Path, inputStream: InputStream, contentLength: Long, keyName: String?) : File {
        if (keyName == null)
            throw UnsupportedMediaType("keyName is null.")
        if (ImageUtils.getFileExtension(keyName)?.toLowerCase() != "mp4"
                && ImageUtils.getFileExtension(keyName)?.toLowerCase() != "avi"
                && ImageUtils.getFileExtension(keyName)?.toLowerCase() != "m4v"
                && ImageUtils.getFileExtension(keyName)?.toLowerCase() != "mov"
        ) throw UnsupportedMediaType("Unsupported media type.")

        return try {
            val result = Files.copy(inputStream, path.resolve(keyName), StandardCopyOption.REPLACE_EXISTING)

            val formatName = ImageUtils.getFileExtension(keyName)
            val inputImage = videoPath.resolve(keyName).toAbsolutePath()
            val outputImage = videoPath.resolve("${System.currentTimeMillis()}.$formatName").toAbsolutePath()

            if(result <= 0){
                Files.deleteIfExists(outputImage)
                throw FileUploadException("Upload file fail.")
            }
            File(Files.move(inputImage, outputImage, StandardCopyOption.REPLACE_EXISTING).toUri())
        } catch (e: IOException) {
            throw RuntimeException("Failed to store file $keyName", e)
        }
    }

    override fun loadAll(path: Path): Stream<Path?>? {
        return try {
            Files.walk(path, 1)
                    .filter { it != path }
                    .map { path.relativize(it) }
        } catch (e: IOException) {
            throw RuntimeException("Failed to read stored files", e)
        }
    }

    override fun load(path: Path, keyName: String): Path? {
        return path.resolve(keyName)
    }

    override fun loadAsResource(path: Path, keyName: String): Resource? {
        return try {
            val file = load(path, keyName) ?: return null
            val resource: Resource = UrlResource(file.toUri())
            if (resource.exists() || resource.isReadable) {
                resource
            } else {
                null
            }
        } catch (e: MalformedURLException) {
            logger.error(e.message, e)
            null
        }
    }

    override fun delete(path: Path, keyName: String) {
        try {
            val file = load(path, keyName) ?: throw IOException()
            Files.delete(file)
        } catch (e: IOException) {
            logger.error(e.message, e)
        }
    }
}
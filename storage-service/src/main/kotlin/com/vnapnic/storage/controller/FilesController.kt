package com.vnapnic.storage.controller

import com.vnapnic.common.models.ErrorCode
import com.vnapnic.common.models.Response
import com.vnapnic.common.models.ResultCode
import com.vnapnic.common.service.JWTService
import com.vnapnic.common.utils.JWTUtils
import com.vnapnic.common.exception.UnsupportedMediaType
import com.vnapnic.common.service.ACCOUNT_ID
import com.vnapnic.common.service.DEVICE_ID
import com.vnapnic.storage.dto.FileDTO
import com.vnapnic.storage.services.FilesStorageService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.SignatureException

@RestController
@RequestMapping("/upload")
class FilesController {
    private val log = LoggerFactory.getLogger(FilesController::class.java)

    @Autowired
    lateinit var storageService: FilesStorageService

    @Autowired
    lateinit var jwtService: JWTService

    @PostMapping("/avatar")
    fun uploadAvatar(
            @RequestHeader headers: MultiValueMap<String, String>,
            @RequestParam("file") file: MultipartFile): Response {
        return try {
            val acceptToken = JWTUtils.tokenFromBearerToken(headers["authorization"]?.get(0))
            val claims = jwtService.parseJWT(acceptToken)
            val accountId = claims?.get(ACCOUNT_ID)
            val deviceId = claims?.get(DEVICE_ID)
            val avatarInfo = storageService.saveAvatar(accountId, deviceId, file)
            storageService.saveAvatarToUser(avatarInfo)
            Response.success(data = FileDTO(
                    fileId = avatarInfo.id,
                    fileName = avatarInfo.fileName,
                    fileExtName = avatarInfo.fileExtName,
                    contentType = avatarInfo.contentType
            ))
        } catch (e: UnsupportedMediaType) {
            e.printStackTrace()
            Response.failed(ResultCode.UNSUPPORTED_MEDIA_TYPE, ErrorCode.UNSUPPORTED_MEDIA_TYPE)
        } catch (e: SignatureException) {
            Response.unauthorized()
        } catch (e: MalformedJwtException) {
            Response.unauthorized()
        } catch (e: ExpiredJwtException) {
            Response.unauthorized()
        } catch (e: UnsupportedJwtException) {
            Response.unauthorized()
        } catch (e: IllegalArgumentException) {
            Response.unauthorized()
        } catch (e: Exception) {
            e.printStackTrace()
            Response.failed(ResultCode.EXPECTATION_FAILED, ErrorCode.FILE_UPLOAD_FAIL)
        }
    }

    @PostMapping("/files")
    fun uploadFiles(
            @RequestHeader headers: MultiValueMap<String, String>,
            @RequestParam("file") file: MultipartFile): Response {
        return try {
            val acceptToken = JWTUtils.tokenFromBearerToken(headers["authorization"]?.get(0))
            val claims = jwtService.parseJWT(acceptToken)
            val accountId = claims?.get(ACCOUNT_ID)
            val deviceId = claims?.get(DEVICE_ID)
            val fileInfo = storageService.saveFiles(accountId, deviceId, file)
            Response.success(data = fileInfo)
        } catch (e: UnsupportedMediaType) {
            e.printStackTrace()
            Response.failed(ResultCode.UNSUPPORTED_MEDIA_TYPE, ErrorCode.UNSUPPORTED_MEDIA_TYPE)
        } catch (e: SignatureException) {
            Response.unauthorized()
        } catch (e: MalformedJwtException) {
            Response.unauthorized()
        } catch (e: ExpiredJwtException) {
            Response.unauthorized()
        } catch (e: UnsupportedJwtException) {
            Response.unauthorized()
        } catch (e: IllegalArgumentException) {
            Response.unauthorized()
        } catch (e: Exception) {
            e.printStackTrace()
            Response.failed(ResultCode.EXPECTATION_FAILED, ErrorCode.FILE_UPLOAD_FAIL)
        }
    }

//    @GetMapping("/fetch/{key:.+}")
//    fun fetch(
//            @RequestHeader headers: MultiValueMap<String, String>,
//            @PathVariable key: String?): ResponseEntity<Resource> {
//         try {
//            if (key == null)
//                return ResponseEntity.notFound().build();
//            if (key.contains("../")) {
//                return ResponseEntity.badRequest().build();
//            }
//
//            val acceptToken = JWTUtils.tokenFromBearerToken(headers["authorization"]?.get(0))
//            val accountId = jwtService.parseJWT(acceptToken)
//            return ResponseEntity.ok().contentType(mediaType).body(file);
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return ResponseEntity.notFound().build();
//        }
//    }
}
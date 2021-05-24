package com.vnapnic.storage.controller

import com.vnapnic.common.beans.ErrorCode
import com.vnapnic.common.beans.Response
import com.vnapnic.common.beans.ResultCode
import com.vnapnic.common.service.JWTService
import com.vnapnic.common.utils.JWTUtils
import com.vnapnic.database.exception.UnsupportedMediaType
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
class UploadController {
    private val log = LoggerFactory.getLogger(UploadController::class.java)

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
            val dto = storageService.saveAvatar(accountId, deviceId, file)
            Response.success(data = dto)
        } catch (e: UnsupportedMediaType) {
            e.printStackTrace()
            Response.failed(ResultCode.UNSUPPORTED_MEDIA_TYPE, ErrorCode.UNSUPPORTED_MEDIA_TYPE)
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
            val dto = storageService.saveFiles(accountId, deviceId, file)
            Response.success(data = dto)
        } catch (e: UnsupportedMediaType) {
            e.printStackTrace()
            Response.failed(ResultCode.UNSUPPORTED_MEDIA_TYPE, ErrorCode.UNSUPPORTED_MEDIA_TYPE)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.failed(ResultCode.EXPECTATION_FAILED, ErrorCode.FILE_UPLOAD_FAIL)
        }
    }
}
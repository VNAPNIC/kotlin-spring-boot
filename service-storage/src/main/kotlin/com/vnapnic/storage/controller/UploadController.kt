package com.vnapnic.storage.controller

import com.vnapnic.common.entities.ErrorCode
import com.vnapnic.common.entities.Response
import com.vnapnic.common.entities.ResultCode
import com.vnapnic.common.service.JWTService
import com.vnapnic.common.utils.JWTUtils
import com.vnapnic.database.exception.UnsupportedMediaType
import com.vnapnic.database.redis.JWT.ACCOUNT_ID
import com.vnapnic.database.redis.JWT.DEVICE_ID
import com.vnapnic.storage.dto.FileResponse
import com.vnapnic.storage.services.FilesStorageService
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import kotlin.collections.ArrayList


@RestController
@RequestMapping("/upload")
class UploadController {
    private val log = LoggerFactory.getLogger(UploadController::class.java)

    @Autowired
    lateinit var storageService: FilesStorageService

    @Autowired
    lateinit var jwtService: JWTService

    @PostMapping("/avatar")
    @ApiOperation(
            value = "fetch file",
            response = FileResponse::class
    )
    fun uploadAvatar(
            @RequestHeader headers: MultiValueMap<String, String>,
            @RequestParam("file") file: MultipartFile): Response<*> {
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
    @ApiOperation(
            value = "fetch file",
            response = FileResponse::class
    )
    fun uploadFiles(
            @RequestHeader headers: MultiValueMap<String, String>,
            @RequestParam("files") files: ArrayList<MultipartFile>): Response<*> {
        return try {
            val acceptToken = JWTUtils.tokenFromBearerToken(headers["authorization"]?.get(0))
            val claims = jwtService.parseJWT(acceptToken)
            val accountId = claims?.get(ACCOUNT_ID)
            val deviceId = claims?.get(DEVICE_ID)

            val results = arrayListOf<FileResponse?>()

            files.stream().forEach {
                val dto = storageService.saveFiles(accountId, deviceId, it)
                results.add(dto)
            }

            Response.success(data = results)
        } catch (e: UnsupportedMediaType) {
            e.printStackTrace()
            Response.failed(ResultCode.UNSUPPORTED_MEDIA_TYPE, ErrorCode.UNSUPPORTED_MEDIA_TYPE)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.failed(ResultCode.EXPECTATION_FAILED, ErrorCode.FILE_UPLOAD_FAIL)
        }
    }
}
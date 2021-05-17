package com.vnapnic.storage.controller

import com.vnapnic.common.models.ErrorCode
import com.vnapnic.common.models.Response
import com.vnapnic.common.models.ResultCode
import com.vnapnic.common.service.JWTService
import com.vnapnic.common.utils.JWTUtils
import com.vnapnic.common.exception.UnsupportedMediaType
import com.vnapnic.storage.services.FilesStorageService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

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
            val accountId = jwtService.parseJWT(acceptToken)
            val avatarInfo = storageService.saveAvatar(accountId, file)
            Response.success(data = avatarInfo)
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
            val accountId = jwtService.parseJWT(acceptToken)
            val fileInfo = storageService.saveFiles(accountId, file)
            Response.success(data = fileInfo)
        } catch (e: UnsupportedMediaType) {
            e.printStackTrace()
            Response.failed(ResultCode.UNSUPPORTED_MEDIA_TYPE, ErrorCode.UNSUPPORTED_MEDIA_TYPE)
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
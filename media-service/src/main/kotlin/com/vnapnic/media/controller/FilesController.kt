package com.vnapnic.media.controller

import com.vnapnic.common.models.ErrorCode
import com.vnapnic.common.models.Response
import com.vnapnic.common.models.ResultCode
import com.vnapnic.media.model.AvatarInfo
import com.vnapnic.media.services.FilesStorageService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/files")
class FilesController {
    private val log = LoggerFactory.getLogger(FilesController::class.java)

    @Autowired
    lateinit var storageService: FilesStorageService

    @PostMapping("/upload/avatar")
    fun uploadAvatar(
            @RequestHeader header: MultiValueMap<String, String>,
            @RequestParam("file") file: MultipartFile): Response<AvatarInfo> {
        try {
            log.info("on zoi may day roiiiiiiiiiiiiiiiii -> ${header["authorization"]}")
            storageService.saveAvatar(file)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(ResultCode.EXPECTATION_FAILED, ErrorCode.FILE_UPLOAD_FAIL)
        }
        return Response.success(data = AvatarInfo())
    }

    @PostMapping
    fun test(@RequestHeader headers: MultiValueMap<String, String>): ResponseEntity<String> {

        headers.forEach {
            println("${it.key}: ${it.value}")
        }
        return ResponseEntity("Found headers with size ${headers.size}", HttpStatus.OK)
    }
}
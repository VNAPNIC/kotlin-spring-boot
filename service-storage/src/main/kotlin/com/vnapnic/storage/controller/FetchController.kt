package com.vnapnic.storage.controller

import com.vnapnic.storage.services.FilesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/fetch")
class FetchController {

    @Autowired
    lateinit var filesService: FilesService

    /**
     * https://github.com/linlinjava/litemall/blob/master/litemall-wx-api/src/main/java/org/linlinjava/litemall/wx/web/WxStorageController.java#L53
     */
    @GetMapping("/avatar/{key:.+}")
    fun fetchAvatar(@PathVariable key: String?) : ResponseEntity<Resource> {
        if (key == null) {
            return ResponseEntity.notFound().build();
        }
        if (key.contains("../")) {
            return ResponseEntity.badRequest().build();
        }
        val fileBean = filesService.avatarFindByKeyName(key)
        val type = fileBean?.contentType ?: return ResponseEntity.notFound().build()
        val mediaType = MediaType.parseMediaType(type)
        val file = filesService.avatarFetch(key) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok().contentType(mediaType).body(file)
    }

    @GetMapping("/file/{key:.+}")
    fun fetchFile(@PathVariable key: String?) : ResponseEntity<Resource>{
        if (key == null) {
            return ResponseEntity.notFound().build();
        }
        if (key.contains("../")) {
            return ResponseEntity.badRequest().build();
        }

        val fileBean = filesService.fileFindByKey(key)
        val type = fileBean?.contentType ?: return ResponseEntity.notFound().build()
        val mediaType = MediaType.parseMediaType(type)
        val file = if (mediaType.type.startsWith("image")){
            filesService.avatarFetch(key) ?: return ResponseEntity.notFound().build()
        }else {
            filesService.videoFetch(key) ?: return ResponseEntity.notFound().build()
        }

        return ResponseEntity.ok().contentType(mediaType).body(file)
    }
}
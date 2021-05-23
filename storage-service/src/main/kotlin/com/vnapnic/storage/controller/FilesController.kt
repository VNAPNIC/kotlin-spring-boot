package com.vnapnic.storage.controller

import com.vnapnic.storage.services.FilesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class FilesController {

    @Autowired
    lateinit var filesService: FilesService

    /**
     * https://github.com/linlinjava/litemall/blob/master/litemall-wx-api/src/main/java/org/linlinjava/litemall/wx/web/WxStorageController.java#L53
     */
    @GetMapping("/fetch/{key:.+}")
    fun fetch(@PathVariable key: String?) : ResponseEntity<Resource>{

        if (key == null) {
            return ResponseEntity.notFound().build();
        }
        if (key.contains("../")) {
            return ResponseEntity.badRequest().build();
        }
//        return ResponseEntity.ok().contentType(mediaType).body(file)
        return  null
    }

    @GetMapping("/download/{key:.+}")
    fun download(@PathVariable key: String) {

    }
}
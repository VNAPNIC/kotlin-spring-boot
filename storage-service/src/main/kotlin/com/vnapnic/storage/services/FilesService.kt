package com.vnapnic.storage.services

import org.springframework.stereotype.Service
import kotlin.jvm.Throws

interface FilesService {
    @Throws(Exception::class)
    fun fetch(key:String)

    @Throws(Exception::class)
    fun download(key:String)
}

@Service
class FilesServiceImpl : FilesService{

    override fun fetch(key: String) {
    }

    override fun download(key: String) {
    }
}
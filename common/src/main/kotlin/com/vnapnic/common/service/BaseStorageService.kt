package com.vnapnic.common.service

import org.springframework.core.io.Resource
import java.io.IOException
import java.io.InputStream
import java.nio.file.Path
import java.util.stream.Stream


interface StorageService {
//    fun store(inputStream: InputStream?, contentLength: Long, contentType: String?, keyName: String?)
//    fun loadAll(): Stream<Path?>?
//    fun load(keyName: String?): Path?
//    fun loadAsResource(keyName: String?): Resource?
//    fun delete(keyName: String?)
//    fun generateUrl(keyName: String?): String?
}

class StorageServiceImpl : StorageService {

//    override fun store(inputStream: InputStream?, contentLength: Long, contentType: String?, keyName: String?) {
//    }
//
//    override fun loadAll(): Stream<Path?>? {
//        return try {
//            Files.walk(rootLocation, 1)
//                    .filter({ path -> !path.equals(rootLocation) })
//                    .map({ path -> rootLocation.relativize(path) })
//        } catch (e: IOException) {
//            throw RuntimeException("Failed to read stored files", e)
//        }
//    }
//
//    override fun load(keyName: String?): Path? {
//    }
//
//    override fun loadAsResource(keyName: String?): Resource? {
//    }
//
//    override fun delete(keyName: String?) {
//    }
//
//    override fun generateUrl(keyName: String?): String? {
//    }
}
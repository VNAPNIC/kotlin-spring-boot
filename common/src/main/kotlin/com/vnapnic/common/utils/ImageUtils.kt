package com.vnapnic.common.utils

import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

object ImageUtils {
    const val AVATAR_URL = "uploads\\avatars"
    const val IMAGE_URL = "uploads\\images"
    const val VIDEO_URL = "uploads\\videos"

    val avatarPath: Path = Paths.get(AVATAR_URL)
    val imagePath: Path = Paths.get(IMAGE_URL)
    val videoPath: Path = Paths.get(VIDEO_URL)

    fun getFileExtension(file: File): String? {
        val fileName: String = file.name
        return if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            fileName.substring(fileName.lastIndexOf(".") + 1)
        else ""
    }

    fun getFileExtension(fileName: String?): String? {
        return if (fileName?.lastIndexOf(".") != -1 && fileName?.lastIndexOf(".") != 0)
            fileName?.substring(fileName.lastIndexOf(".") + 1)
        else ""
    }
}
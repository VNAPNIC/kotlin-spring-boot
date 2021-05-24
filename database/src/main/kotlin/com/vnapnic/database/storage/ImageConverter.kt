package com.vnapnic.database.storage

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.jvm.Throws

object ImageConverter {
    @Throws
    fun convertType(inputImage: String, outputImage: String, formatName: String): File {
        val fileInputStream = FileInputStream(inputImage)
        val image = ImageIO.read(fileInputStream)
        fileInputStream.close() // ImageIO.read does not close the input stream

        val convertedImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)
        convertedImage.createGraphics().drawImage(image, 0, 0, Color(0, 0, 0, 0), null)
        val fileOutputStream = FileOutputStream(outputImage)
        val canWrite = ImageIO.write(convertedImage, formatName, fileOutputStream)
        fileOutputStream.close() // ImageIO.write does not close the output stream

        if (!canWrite) {
            throw IllegalStateException("Failed to write image.")
        } else {
            Files.delete(Paths.get(inputImage))
        }

        return File(outputImage)
    }
}
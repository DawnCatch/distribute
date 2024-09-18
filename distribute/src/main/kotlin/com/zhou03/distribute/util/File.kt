package com.zhou03.distribute.util

import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException

fun resourceFolderBasePath(path: String) = "${path}file"

fun upload(path: String, file: MultipartFile): String? {
    val suffix = file.originalFilename!!.split(".").last()
    val filename = "${uuid()}.$suffix"
    val fileUrl = "${resourceFolderBasePath(path)}/${filename}"
    val newFile = java.io.File(fileUrl)
    try {
        file.transferTo(newFile)
    } catch (e: IOException) {
        return null
    }
    return filename
}

fun upload(path: String, files: List<MultipartFile>): List<String> {
    val result = mutableListOf<String>()
    files.forEach { file ->
        val value = upload(path, file) ?: return@forEach
        result += value
    }
    return result
}

fun delete(path: String, filename: String): Boolean {
    val fileUrl = "${resourceFolderBasePath(path)}/${filename}"
    val file = File(fileUrl)
    if (!file.exists()) return true
    return file.delete()
}
package com.vnapnic.common.db.files

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "files")
data class FileInfo(@Id var id: String? = null) : MediasInfo()
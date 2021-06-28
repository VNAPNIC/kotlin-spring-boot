package com.vnapnic.database.entities.files

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "files")
data class FileInfoEntity(@Id var id: String? = null) : BaseFileEntity()
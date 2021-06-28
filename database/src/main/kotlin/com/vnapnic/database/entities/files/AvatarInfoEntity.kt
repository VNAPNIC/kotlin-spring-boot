package com.vnapnic.database.entities.files

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "avatar")
data class AvatarInfoEntity(@Id var id: String? = null) : BaseFileEntity()
package com.vnapnic.media.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "avatar")
data class AvatarInfo(@Id var id: String? = null) : FileInfo()
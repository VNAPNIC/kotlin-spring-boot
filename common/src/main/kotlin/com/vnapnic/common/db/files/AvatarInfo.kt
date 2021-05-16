package com.vnapnic.common.db.files

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "avatar")
data class AvatarInfo(@Id var id: String? = null) : MediasInfo()
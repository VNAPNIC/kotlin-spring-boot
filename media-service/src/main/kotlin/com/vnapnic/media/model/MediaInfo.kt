package com.vnapnic.media.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "media")
data class MediaInfo(@Id var id: String? = null) : FileInfo()
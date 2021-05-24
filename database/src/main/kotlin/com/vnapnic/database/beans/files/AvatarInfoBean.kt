package com.vnapnic.database.beans.files

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "avatar")
data class AvatarInfoBean(@Id var id: String? = null) : BaseFileBean()
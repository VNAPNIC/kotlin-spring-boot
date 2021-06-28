package com.vnapnic.database.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "sequence")
data class SequenceIdEntity(@Id var id: String = "", var seq: Int? = 0)
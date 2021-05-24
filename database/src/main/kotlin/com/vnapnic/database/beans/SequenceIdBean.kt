package com.vnapnic.database.beans

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "sequence")
data class SequenceIdBean(@Id var id: String = "", var seq: Int? = 0)
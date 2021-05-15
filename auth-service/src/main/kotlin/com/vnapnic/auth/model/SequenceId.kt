package com.vnapnic.auth.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "sequence")
data class SequenceId(@Id var id: String = "", var seq: Int? = 0)
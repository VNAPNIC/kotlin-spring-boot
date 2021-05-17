package com.vnapnic.common.db

import com.fasterxml.jackson.annotation.JsonClassDescription
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class CleanTool(@JsonProperty("id")
                     @Id
                     var id: String = "",
                    var name:String? =null,
                     var description: String? = null
                    )
package com.vnapnic.user.dto

import com.vnapnic.database.entities.files.AvatarInfoEntity
import com.vnapnic.database.enums.Gender

data class UserResponse(
        var userId: String? = null,
        var firstName: String? = null,
        var lastName: String? = null,
        var weight: Double? = 0.0, // grams
        var height: Double? = 0.0, // cm
        var gender: Gender? = Gender.OTHER,
        var description: String? = null,
        var avatar: AvatarInfoEntity? = null
)
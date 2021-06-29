package com.vnapnic.common.dto

import com.vnapnic.database.entities.UserEntity
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
) {
    companion object {
        fun from(entity: UserEntity?): UserResponse = UserResponse(
                userId = entity?.id,
                firstName = entity?.firstName,
                lastName = entity?.lastName,
                weight = entity?.weight ?: 0.0,
                height = entity?.height ?: 0.0,
                gender = entity?.gender,
                description = entity?.description,
                avatar = entity?.avatar
        )
    }
}


package com.vnapnic.common.dto

import com.vnapnic.database.entities.AccountEntity
import com.vnapnic.database.enums.Role
import java.util.*

data class AccountResponse(var id: String? = null,
                           val socialId: String? = null,
                           val phoneNumber: String? = null,
                           val email: String? = null,
                           val active: Boolean? = false,
                           val emailVerified: Boolean? = false,
                           val phoneVerified: Boolean? = false,
                           val registerTime: Date? = null,
                           val emailVerifiedTime: Date? = null,
                           val phoneVerifiedTime: Date? = null,
                           val collaboratorId: String? = null,
                           val role: Role? = Role.UNKNOWN,
                           val user: UserResponse? = null
) {
    companion object {
        fun from(entity: AccountEntity?): AccountResponse? = AccountResponse(
                id = entity?.id,
                socialId = entity?.socialId,
                email = entity?.email,
                phoneNumber = entity?.phoneNumber,
                active = entity?.active,

                emailVerified = entity?.emailVerified,
                phoneVerified = entity?.phoneVerified,

                registerTime = entity?.registerTime,
                emailVerifiedTime = entity?.emailVerifiedTime,
                phoneVerifiedTime = entity?.phoneVerifiedTime,

                collaboratorId = entity?.collaboratorId,
                role = entity?.role,
                user = UserResponse.from(entity?.info)
        )
    }
}


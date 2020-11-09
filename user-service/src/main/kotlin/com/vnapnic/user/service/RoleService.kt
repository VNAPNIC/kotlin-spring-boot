package com.vnapnic.user.service

import com.vnapnic.user.model.user.entity.Role

interface RoleService {
    fun getAllRoles() : List<Role>
}
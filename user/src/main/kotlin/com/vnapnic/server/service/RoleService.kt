package com.vnapnic.server.service

import com.vnapnic.server.model.user.entity.Role

interface RoleService {
    fun getAllRoles() : List<Role>
}
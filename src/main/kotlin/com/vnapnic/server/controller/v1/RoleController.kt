package com.vnapnic.server.controller.v1

import com.vnapnic.server.model.user.entity.Role

interface RoleController {
    fun getAllRole(): List<Role>
}
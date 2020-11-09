package com.vnapnic.server.controller.v1

import com.vnapnic.server.model.user.entity.Role
import org.springframework.http.ResponseEntity

interface RoleController {
    fun getAllRole(): ResponseEntity<List<Role>>
}
package com.vnapnic.user.controller.v1.role

import com.vnapnic.user.model.user.entity.Role
import org.springframework.http.ResponseEntity

interface RoleController {
    fun getAllRole(): ResponseEntity<List<Role>>
}
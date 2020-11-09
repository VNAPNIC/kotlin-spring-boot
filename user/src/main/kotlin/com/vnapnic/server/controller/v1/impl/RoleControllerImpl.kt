package com.vnapnic.server.controller.v1.impl

import com.vnapnic.server.controller.v1.RoleController
import com.vnapnic.server.model.user.entity.Role
import com.vnapnic.server.service.RoleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1")
class RoleControllerImpl(private val  service: RoleService) : RoleController {

    @GetMapping("/roles")
    override fun getAllRole() = ResponseEntity(service.getAllRoles(), HttpStatus.OK)
}
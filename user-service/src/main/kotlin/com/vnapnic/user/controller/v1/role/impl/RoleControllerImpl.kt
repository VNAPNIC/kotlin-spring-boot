package com.vnapnic.user.controller.v1.role.impl

import com.vnapnic.user.controller.v1.role.RoleController
import com.vnapnic.user.service.role.RoleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/roles")
class RoleControllerImpl(private val  service: RoleService) : RoleController {

    @GetMapping("/roles")
    @PreAuthorize("#oauth2.hasAnyScope('Visitor-Restricted','User-WEB','User-MOBILE')")
    override fun getAllRole() = ResponseEntity(service.getAllRoles(), HttpStatus.OK)
}
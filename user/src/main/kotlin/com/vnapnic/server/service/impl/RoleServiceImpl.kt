package com.vnapnic.server.service.impl

import com.vnapnic.server.model.user.entity.Role
import com.vnapnic.server.repository.RoleRepository
import com.vnapnic.server.service.RoleService
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl(private val repository: RoleRepository) : RoleService{
    override fun getAllRoles(): List<Role> = repository.findAll().toList()
}
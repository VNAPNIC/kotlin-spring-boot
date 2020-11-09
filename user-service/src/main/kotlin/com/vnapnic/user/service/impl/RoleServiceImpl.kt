package com.vnapnic.user.service.impl

import com.vnapnic.user.model.user.entity.Role
import com.vnapnic.user.repository.RoleRepository
import com.vnapnic.user.service.RoleService
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl(private val repository: RoleRepository) : RoleService{
    override fun getAllRoles(): List<Role> = repository.findAll().toList()
}
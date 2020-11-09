package com.vnapnic.server.repository

import com.vnapnic.server.model.user.entity.Role
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : CrudRepository<Role, Long> {
    fun findByRole(role: String): Role
}
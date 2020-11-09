package com.vnapnic.user.repository

import com.vnapnic.user.model.user.entity.Role
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : CrudRepository<Role, Long> {
    fun findByRole(role: String): Role
}
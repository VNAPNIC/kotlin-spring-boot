package com.vnapnic.user.model.user.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.stream.Collectors


class CustomUserDetails(user: User) : User(user), UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return roles.stream()
                .map { role -> SimpleGrantedAuthority(role.role?.name) }
                .collect(Collectors.toList())
    }

    override fun getPassword(): String {
        return super._password
    }

    override fun getUsername(): String = super._username

    override fun isAccountNonExpired(): Boolean = super.active

    override fun isAccountNonLocked(): Boolean = super.enable

    override fun isCredentialsNonExpired(): Boolean = super.active

    override fun isEnabled(): Boolean = super.isEmailVerified

}

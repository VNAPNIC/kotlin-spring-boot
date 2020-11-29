package com.vnapnic.user.model.user.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.NaturalId
import javax.persistence.*

enum class RoleName {
    ROLE_ADMIN,
    ROLE_USER
}

@Entity(name = "role")
data class Role(@GeneratedValue(strategy = GenerationType.IDENTITY)
                @Id
                @Column(name = "role_id")
                var id: Long? = null,

                @Column(name = "role_name", unique = true, length = 100)
                @Enumerated(EnumType.STRING)
                @NaturalId
                var role: RoleName? = null,

                @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
                @JsonIgnore
                var userList: MutableList<User> = mutableListOf()
) : java.io.Serializable
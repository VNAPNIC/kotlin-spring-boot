package com.vnapnic.user.model.user.entity

import com.vnapnic.common.model.DateAudit
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity(name = "user")
open class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id")
        var id: Long? = null,

        @Id
        @Column(name = "public_key", unique = true, length = 100)
        var userId: String? = null,

        @Column(name = "username", unique = true, length = 100)
        @NotBlank(message = "User name cannot be null")
        var _username: String = "",

        @Column(name = "password", unique = true, length = 100)
        @NotBlank(message = "password cannot be null")
        var _password: String = "",

        @Column(name = "email", length = 100)
        var email: String? = null,

        @Column(name = "phone", length = 100)
        var phone: String? = null,

        @Column(name = "avatar")
        var avatar: String? = null,

        @Column(name = "enable", nullable = false)
        var enable: Boolean = false,

        @Column(name = "active", nullable = false)
        var active: Boolean = false,

        @Column(name = "isEmailVerified", nullable = false)
        var isEmailVerified: Boolean = false,

        @Version
        var version: Int = 0,

        @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
        @JoinTable(name = "user_auth", joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "user_id")],
                inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "role_id")]
        )
        var roles: Set<Role> = HashSet()
) : DateAudit(), java.io.Serializable {

    constructor(user: User) : this() {
        id = user.id
        userId = user.userId
        _username = user._username
        _password = user._password
        email = user.email
        phone = user.phone
        avatar = user.avatar
        enable = user.enable
        active = user.active
        isEmailVerified = user.isEmailVerified
        version = user.version
        roles = user.roles
    }

    override fun toString(): String {
        return "User(id=$id, " +
                "userId=$userId, " +
                "_username='$_username', " +
                "_password='$_password', " +
                "email=$email, " +
                "phone=$phone, " +
                "avatar=$avatar, " +
                "enable=$enable, active=$active, " +
                "isEmailVerified=$isEmailVerified, " +
                "version=$version, " +
                "roles=$roles)"
    }

}
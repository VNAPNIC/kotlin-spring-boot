package com.vnapnic.user.model.user.entity

import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.Version

@Entity
@Table(name = "users")
data class User(@GeneratedValue(strategy = GenerationType.IDENTITY)
                @Id var id: Int? = null,
                var userId: String = "",
                var username: String = "",
                var password: String = "",
                var email: String = "",
                var phone: String? = null,
                var avatar: String? = null,
                var createTime: ZonedDateTime = ZonedDateTime.now(),
                var enable: Boolean = true,
                @JoinColumn(name = "role_id", referencedColumnName = "id")
                @OneToOne var role: Role? = null,
                @Version var version: Int = 0) : java.io.Serializable
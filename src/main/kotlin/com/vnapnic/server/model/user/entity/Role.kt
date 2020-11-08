package com.vnapnic.server.model.user.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "roles")
data class Role(@GeneratedValue(strategy = GenerationType.IDENTITY)
                @Id var id: Int? = null,
                var role: String = "") : java.io.Serializable
package com.vnapnic.common.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.Instant
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@JsonIgnoreProperties(value = ["created", "updated"], allowGetters = true)
open class DateAudit(@CreatedDate
                     @Column(nullable = false, updatable = false)
                     private val created: Instant? = null,
                     @LastModifiedDate
                     @Column(nullable = false)
                     private val updated: Instant? = null) : Serializable
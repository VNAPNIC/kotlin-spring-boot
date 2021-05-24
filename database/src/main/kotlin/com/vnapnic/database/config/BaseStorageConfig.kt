package com.vnapnic.database.config

import com.vnapnic.database.storage.LocalStorage
import com.vnapnic.database.storage.Storage
import org.springframework.context.annotation.Bean

open class BaseStorageConfig {
    @Bean
    open fun localStorage(): Storage = LocalStorage()
}
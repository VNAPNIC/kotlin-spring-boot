package com.vnapnic.user.model.evironment

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "environment-params")
data class EnvironmentParams(
        val verifyCodeValidSeconds: Long,
        val verifyCodeRetryMax: Int,
        val webClientSecret: String,
        val webAccessValiditySeconds: Int,
        val webRefreshValiditySeconds: Int,
        val mobileAccessValiditySeconds: Int,
        val mobileRefreshValiditySeconds: Int
)
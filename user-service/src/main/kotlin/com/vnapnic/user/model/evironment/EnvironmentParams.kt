package com.vnapnic.user.model.evironment

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "environment-params")
data class EnvironmentParams(val verifyCodeValidSeconds: Long,
                             val verifyCodeRetryMax: Int,
                             val webClientId: String,
                             val webClientSecret: String,
                             val webClientScopes: String,
                             val webAccessValiditySeconds: Int,
                             val webRefreshValiditySeconds: Int,
                             val mobileClientId: String,
                             val mobileClientSecret: String,
                             val mobileClientScopes: String,
                             val mobileAccessValiditySeconds: Int,
                             val mobileRefreshValiditySeconds: Int,
                             val visitorClientId: String,
                             val visitorClientSecret: String,
                             val visitorClientScopes: String,
                             val visitorAccessValiditySeconds: Int,
                             val visitorRefreshValiditySeconds: Int,
                             val serverInternalId: String,
                             val serverInternalSecret: String) : java.io.Serializable
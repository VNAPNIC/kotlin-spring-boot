package com.vnapnic.configuration.security

object SecurityWhiteList {
    val SECURITY_WHITE_LIST = arrayOf(
            "/test/**",
            "/auth/**",
            "/swagger**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/static/**",
            "/js/*",
            "/css/**",
            "/webjars/**",
            "/v2/api-docs/**",
            "/v3/api-docs/**",
            "/doc.html"
    )
}
package com.vnapnic.gateway

//import org.springframework.context.annotation.Configuration
//import org.springframework.http.HttpMethod
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
//import org.springframework.security.config.http.SessionCreationPolicy

//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//class RestSecurityConfig : WebSecurityConfigurerAdapter() {
//
//    override fun configure(http: HttpSecurity?) {
//        http
//                // we don't need CSRF because our token is invulnerable
//                ?.csrf()?.disable()
//                // don't create session
//                ?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)?.and()
//                ?.authorizeRequests()
//                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                // allow anonymous resource requests
//                ?.antMatchers(
//                        HttpMethod.GET,
//                        "/"
//                )?.permitAll()
//    }
//}
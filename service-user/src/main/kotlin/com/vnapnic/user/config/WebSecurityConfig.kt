package com.vnapnic.user.config

import com.vnapnic.configuration.security.SecurityWhiteList.SECURITY_WHITE_LIST
import com.vnapnic.user.security.JwtAuthenticationEntryPoint
import com.vnapnic.user.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
class WebSecurityConfig(private val jwtEntryPoint: JwtAuthenticationEntryPoint) : WebSecurityConfigurerAdapter() {

    /**Through reloading, configure how to protect requests through interceptors*/
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity?) {
        http!!.cors()
                .and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
                .and()
                /*authentication*/
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(*SECURITY_WHITE_LIST).permitAll()
                .antMatchers(HttpMethod.POST, "/**/**/**/user").permitAll()
                .antMatchers(HttpMethod.GET, "/**/**/**/roles").permitAll()
                .anyRequest().authenticated()

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter? = JwtAuthenticationFilter()

    @Bean
    fun passwordEncoder(): PasswordEncoder? = BCryptPasswordEncoder()
}
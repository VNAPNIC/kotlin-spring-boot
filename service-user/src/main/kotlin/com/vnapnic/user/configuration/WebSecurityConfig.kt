package com.vnapnic.user.configuration

import com.vnapnic.user.configuration.jwt.JwtAccessDeniedHandler
import com.vnapnic.user.configuration.jwt.JwtAuthenticationEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.savedrequest.NullRequestCache
import org.springframework.web.bind.annotation.RequestMethod
import java.security.SecureRandom

@EnableWebSecurity
@Configuration
class WebSecurityConfig(
        private val jwtEntryPoint: JwtAuthenticationEntryPoint
//        private val jwtAccessDenied: JwtAccessDeniedHandler
) : WebSecurityConfigurerAdapter() {

    companion object {
        /**Authentication whitelist, no verification required*/
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

    /**Through reloading, configure how to protect requests through interceptors*/
    override fun configure(http: HttpSecurity?) {
        http!!.cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(*SECURITY_WHITE_LIST).permitAll()
                .antMatchers(HttpMethod.POST, "/**/**/**/user").permitAll()
                .antMatchers(HttpMethod.GET, "/**/**/**/roles").permitAll()
                .anyRequest().authenticated()
                .and()
                .requestCache().requestCache(NullRequestCache()).and()
                /*authentication*/
//                .addFilter(JwtLoginFilter(authenticationManager(), authService))
//                .addFilter(JwtAuthorizationFilter(authenticationManager(), userDetailsServiceImpl, authService))
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
                /*logout*/
//                .logout()
//                .logoutUrl(SecurityConstants.AUTH_LOGOUT_URL)
//                .logoutSuccessUrl(SecurityConstants.AUTH_LOGOUT_SUCCESS_URL)
//                .logoutSuccessHandler(JwtLogoutSuccessHandler())
//                .addLogoutHandler(JwtLogoutHandler())
//                .and()
                /*Authorization exception handling*/
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
//                .accessDeniedHandler(jwtAccessDenied)

        //Prevent the Frame of web page from being intercepted
        http.headers().frameOptions().disable()
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? = BCryptPasswordEncoder()
}
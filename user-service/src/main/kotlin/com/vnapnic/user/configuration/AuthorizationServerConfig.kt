package com.vnapnic.user.configuration

import com.vnapnic.user.model.evironment.EnvironmentParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore

@EnableAuthorizationServer
@Configuration
class AuthorizationServerConfig : AuthorizationServerConfigurerAdapter() {

    @Autowired
    private lateinit var redisConnectionFactory: RedisConnectionFactory

    @Autowired
    private lateinit var environment: EnvironmentParams

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients!!.inMemory()
                .withClient(environment.webClientId)
                .secret(environment.webClientSecret)
                .authorizedGrantTypes("password", "refresh_token")
                .scopes(environment.webClientScopes)
                .accessTokenValiditySeconds(environment.webAccessValiditySeconds)
                .refreshTokenValiditySeconds(environment.webRefreshValiditySeconds)
                // Mobile client
                .and()
                .withClient(environment.mobileClientId)
                .secret(environment.mobileClientSecret)
                .authorizedGrantTypes("password", "refresh_token")
                .scopes(environment.mobileClientScopes)
                .accessTokenValiditySeconds(environment.mobileAccessValiditySeconds)
                .refreshTokenValiditySeconds(environment.mobileRefreshValiditySeconds)
                // Visitor client
                .and()
                .withClient(environment.visitorClientId)
                .secret(environment.visitorClientSecret)
                .authorizedGrantTypes("client_credentials")
                .scopes(environment.visitorClientScopes)
                .accessTokenValiditySeconds(environment.visitorAccessValiditySeconds)
                .refreshTokenValiditySeconds(environment.visitorRefreshValiditySeconds)
                .and()
                // Internal server
                .withClient(environment.serverInternalId)
                .secret(environment.serverInternalSecret)
    }

    override fun configure(security: AuthorizationServerSecurityConfigurer?) {
        security!!.tokenKeyAccess("permitAll()") // /oauth/token_key
                .checkTokenAccess("isAuthenticated()") // /oauth/check_token
                .passwordEncoder(passwordEncoder())
                .allowFormAuthenticationForClients()
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
        endpoints!!.authenticationManager(authenticationManager)
                .tokenStore(RedisTokenStore(redisConnectionFactory))
    }
}
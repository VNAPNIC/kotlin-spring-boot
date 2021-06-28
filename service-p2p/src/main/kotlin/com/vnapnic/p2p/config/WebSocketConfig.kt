package com.vnapnic.p2p.config

import com.vnapnic.p2p.component.CallHandler
import com.vnapnic.p2p.component.SignalHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor


@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {
    @Autowired
    lateinit var signalHandler: SignalHandler

    @Autowired
    lateinit var callHandler: CallHandler

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(signalHandler, "/signal")
                .addHandler(callHandler, "/call")
                .addInterceptors(HttpSessionHandshakeInterceptor()).setAllowedOrigins("*")
    }

    @Bean
    fun createWebSocketContainer(): ServletServerContainerFactoryBean? {
        val container = ServletServerContainerFactoryBean()
        container.setMaxTextMessageBufferSize(8192)
        container.setMaxBinaryMessageBufferSize(8192)
        return container
    }
}
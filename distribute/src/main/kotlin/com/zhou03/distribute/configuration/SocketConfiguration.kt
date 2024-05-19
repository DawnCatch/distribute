package com.zhou03.distribute.configuration

import com.zhou03.distribute.controller.ChatWebSocket
import com.zhou03.distribute.interceptor.ChatWebSocketInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry


@Configuration
@EnableWebSocket
class SocketConfiguration : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(ChatWebSocket(), "/chat")
            .setAllowedOrigins("*")
            .addInterceptors(ChatWebSocketInterceptor())
    }
}
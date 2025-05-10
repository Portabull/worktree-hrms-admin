package com.worktree.hrms.config;

import com.worktree.hrms.handlers.NotificationWebsocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        String allowCustomOrigin = System.getProperty("allow.custom.origin");
        registry.addHandler(notificationWebsocketHandler(), "/api/ws/notification")
                .setAllowedOrigins(allowCustomOrigin == null ? "worktree-training.space" : allowCustomOrigin);
    }

    @Bean
    public NotificationWebsocketHandler notificationWebsocketHandler() {
        return new NotificationWebsocketHandler();
    }

}
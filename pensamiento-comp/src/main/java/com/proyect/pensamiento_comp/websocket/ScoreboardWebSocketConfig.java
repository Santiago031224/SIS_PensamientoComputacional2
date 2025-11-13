package com.proyect.pensamiento_comp.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class ScoreboardWebSocketConfig implements WebSocketConfigurer {

    private final ScoreboardWebSocketHandler scoreboardWebSocketHandler;

    public ScoreboardWebSocketConfig(ScoreboardWebSocketHandler scoreboardWebSocketHandler) {
        this.scoreboardWebSocketHandler = scoreboardWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(scoreboardWebSocketHandler, "/ws/scoreboard")
                .setAllowedOriginPatterns("*");
    }
}

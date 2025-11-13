package com.proyect.pensamiento_comp.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScoreboardPublisher {

    private static final Logger log = LoggerFactory.getLogger(ScoreboardPublisher.class);

    private final ScoreboardWebSocketHandler handler;

    public ScoreboardPublisher(ScoreboardWebSocketHandler handler) {
        this.handler = handler;
    }

    public void publish(ScoreboardEvent event) {
        if (!handler.hasActiveSessions()) {
            log.debug("Skipping scoreboard broadcast: no active sessions");
        }
        handler.broadcast(event);
    }
}

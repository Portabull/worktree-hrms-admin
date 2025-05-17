package com.worktree.hrms.handlers;

import com.worktree.hrms.utils.EncryptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class NotificationWebsocketHandler extends TextWebSocketHandler {


    private static Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("New WebSocket connection established: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
        log.info("WebSocket connection closed: {}", session.getId());
    }

    public void sendNotification(String message) {
        String encryptedBody = EncryptionUtils.encrypt(message);
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new BinaryMessage(encryptedBody.getBytes()));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

}

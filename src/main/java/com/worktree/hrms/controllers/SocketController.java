package com.worktree.hrms.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worktree.hrms.handlers.NotificationWebsocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SocketController {

    private final NotificationWebsocketHandler notificationWebsocketHandler;

    private static final String METHOD = "method";

    @PostMapping("push-notification")
    public void asas(@RequestBody Map<String, String> payload) throws JsonProcessingException {

        notificationWebsocketHandler.sendNotification(new ObjectMapper().writeValueAsString(Map.of("alert", payload.get("alert"),
                "message", payload.get("message"), "type",
                payload.get("type"), METHOD, payload.get(METHOD) != null ? payload.get(METHOD) : "handleDefaultNotificationEvents")));
    }

}

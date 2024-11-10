package com.worktree.hrms.controllers;

import com.worktree.hrms.constants.CommonConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api")
public class ConfigurationController {

    private Map<String, Object> emailPayload;
    private Map<String, Object> aiPayload;

    @PostMapping("email/config")
    public ResponseEntity saveEmailConfiguration(@RequestBody Map<String, Object> payload) {
        this.emailPayload = payload;
        return new ResponseEntity(CommonConstants.SUCCESS_RESPONSE, HttpStatus.OK);
    }

    @GetMapping("email/config")
    public ResponseEntity<?> getEmailConfiguration() {
        if (emailPayload == null) {
            emailPayload = new HashMap<>();
            emailPayload.put("emailHost", "smtp.gmail.com");
            emailPayload.put("emailPort", "587");
            emailPayload.put("username", "portablemanagementsystems@gmail.com");
            emailPayload.put("password", "ahwwyteyigkyixir");
            emailPayload.put("emailFrom", "portablemanagementsystems@gmail.com");
            emailPayload.put("additionalProperties", Arrays.asList(
                    Map.of("name", "mail.transport.protocol", "value", "smtp"),
                    Map.of("name", "mail.smtp.auth", "value", "true"),
                    Map.of("name", "mail.smtp.starttls.enable", "value", "true"),
                    Map.of("name", "mail.debug", "value", "false")
            ));
        }
        return new ResponseEntity(emailPayload, HttpStatus.OK);
    }

    @PostMapping("ai/config")
    public ResponseEntity saveAIConfiguration(@RequestBody Map<String, Object> payload) {
        this.aiPayload = payload;
        return new ResponseEntity(CommonConstants.SUCCESS_RESPONSE, HttpStatus.OK);
    }

    @GetMapping("ai/config")
    public ResponseEntity<?> getAIConfiguration() {
        if (aiPayload == null) {
            aiPayload = new HashMap<>();
            aiPayload.put("provider", "openai");
            aiPayload.put("key", "HDUFGJKNHGNDKW");
            aiPayload.put("model", "gpt-4o");
            aiPayload.put("version", "2045.01.2");
        }
        return new ResponseEntity(aiPayload, HttpStatus.OK);
    }

}

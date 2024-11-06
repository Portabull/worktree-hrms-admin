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

    private Map<String, Object> payload;

    @PostMapping("email/config")
    public ResponseEntity saveEmailConfiguration(@RequestBody Map<String, Object> payload) {
        this.payload = payload;
        return new ResponseEntity(CommonConstants.SUCCESS_RESPONSE, HttpStatus.OK);
    }

    @GetMapping("email/config")
    public ResponseEntity<?> getEmailConfiguration() {
        if (payload == null) {
            payload = new HashMap<>();
            payload.put("emailHost", "smtp.gmail.com");
            payload.put("emailPort", "587");
            payload.put("username", "portablemanagementsystems@gmail.com");
            payload.put("password", "ahwwyteyigkyixir");
            payload.put("emailFrom", "portablemanagementsystems@gmail.com");
            payload.put("additionalProperties", Arrays.asList(
                    Map.of("name", "mail.transport.protocol", "value", "smtp"),
                    Map.of("name", "mail.smtp.auth", "value", "true"),
                    Map.of("name", "mail.smtp.starttls.enable", "value", "true"),
                    Map.of("name", "mail.debug", "value", "false")
            ));
        }
        return new ResponseEntity(payload, HttpStatus.OK);
    }

}

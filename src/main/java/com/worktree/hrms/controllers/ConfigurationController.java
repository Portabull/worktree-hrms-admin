package com.worktree.hrms.controllers;

import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    @PostMapping("email/config")
    public ResponseEntity<Map<String, Object>> saveEmailConfiguration(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity(configurationService.saveEmailConfiguration(payload), HttpStatus.OK);
    }

    @GetMapping("email/config")
    public ResponseEntity<?> getEmailConfiguration() {
        return new ResponseEntity(configurationService.getEmailConfiguration(), HttpStatus.OK);
    }

    @PostMapping("ai/config")
    public ResponseEntity saveAIConfiguration(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity(configurationService.saveAIConfiguration(payload), HttpStatus.OK);
    }

    @GetMapping("ai/config")
    public ResponseEntity<?> getAIConfiguration() {
        return new ResponseEntity(configurationService.getAIConfiguration(), HttpStatus.OK);
    }

}

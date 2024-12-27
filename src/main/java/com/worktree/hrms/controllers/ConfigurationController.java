package com.worktree.hrms.controllers;

import com.worktree.hrms.annotations.Feature;
import com.worktree.hrms.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    @Feature
    @PostMapping("email/config")
    public ResponseEntity<Map<String, Object>> saveEmailConfiguration(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<>(configurationService.saveEmailConfiguration(payload), HttpStatus.OK);
    }

    @Feature
    @GetMapping("email/config")
    public ResponseEntity<Map<String, Object>> getEmailConfiguration() {
        return new ResponseEntity<>(configurationService.getEmailConfiguration(), HttpStatus.OK);
    }

    @Feature
    @PostMapping("ai/config")
    public ResponseEntity<Map<String, Object>> saveAIConfiguration(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<>(configurationService.saveAIConfiguration(payload), HttpStatus.OK);
    }

    @Feature
    @GetMapping("ai/config")
    public ResponseEntity<Map<String, Object>> getAIConfiguration() {
        return new ResponseEntity<>(configurationService.getAIConfiguration(), HttpStatus.OK);
    }

    @Feature
    @PostMapping("mobile/config")
    public ResponseEntity<Map<String, Object>> saveMobileConfiguration(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<>(configurationService.saveMobileConfiguration(payload), HttpStatus.OK);
    }

    @Feature
    @GetMapping("mobile/config")
    public ResponseEntity<Map<String, Object>> getMobileConfiguration() {
        return new ResponseEntity<>(configurationService.getMobileConfiguration(), HttpStatus.OK);
    }

    @Feature
    @PostMapping("storage/config")
    public ResponseEntity<Map<String, Object>> saveStorageConfiguration(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<>(configurationService.saveStorageConfiguration(payload), HttpStatus.OK);
    }

    @Feature
    @GetMapping("storage/config")
    public ResponseEntity<Map<String, Object>> getStorageConfiguration() {
        return new ResponseEntity<>(configurationService.getStorageConfiguration(), HttpStatus.OK);
    }

}

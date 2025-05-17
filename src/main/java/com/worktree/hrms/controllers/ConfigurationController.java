package com.worktree.hrms.controllers;

import com.worktree.hrms.annotations.Feature;
import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @Feature(feature = CommonConstants.Features.EMAIL_SETTINGS)
    @PostMapping("email/config")
    public ResponseEntity<Map<String, Object>> saveEmailConfiguration(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<>(configurationService.saveEmailConfiguration(payload), HttpStatus.OK);
    }

    @Feature(feature = CommonConstants.Features.EMAIL_SETTINGS)
    @GetMapping("email/config")
    public ResponseEntity<Map<String, Object>> getEmailConfiguration() {
        return new ResponseEntity<>(configurationService.getEmailConfiguration(), HttpStatus.OK);
    }

    @Feature(feature = CommonConstants.Features.AI_SETTINGS)
    @PostMapping("ai/config")
    public ResponseEntity<Map<String, Object>> saveAIConfiguration(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<>(configurationService.saveAIConfiguration(payload), HttpStatus.OK);
    }

    @Feature(feature = CommonConstants.Features.AI_SETTINGS)
    @GetMapping("ai/config")
    public ResponseEntity<Map<String, Object>> getAIConfiguration() {
        return new ResponseEntity<>(configurationService.getAIConfiguration(), HttpStatus.OK);
    }

    @Feature(feature = CommonConstants.Features.MOBILE_SETTINGS)
    @PostMapping("mobile/config")
    public ResponseEntity<Map<String, Object>> saveMobileConfiguration(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<>(configurationService.saveMobileConfiguration(payload), HttpStatus.OK);
    }

    @Feature(feature = CommonConstants.Features.MOBILE_SETTINGS)
    @GetMapping("mobile/config")
    public ResponseEntity<Map<String, Object>> getMobileConfiguration() {
        return new ResponseEntity<>(configurationService.getMobileConfiguration(), HttpStatus.OK);
    }

    @Feature(feature = CommonConstants.Features.STORAGE_SETTINGS)
    @PostMapping("storage/config")
    public ResponseEntity<Map<String, Object>> saveStorageConfiguration(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<>(configurationService.saveStorageConfiguration(payload), HttpStatus.OK);
    }

    @Feature(feature = CommonConstants.Features.STORAGE_SETTINGS)
    @GetMapping("storage/config")
    public ResponseEntity<Map<String, Object>> getStorageConfiguration() {
        return new ResponseEntity<>(configurationService.getStorageConfiguration(), HttpStatus.OK);
    }

    @Feature(feature = CommonConstants.Features.PROXY_SETTINGS)
    @PostMapping("proxy/config")
    public ResponseEntity<Map<String, Object>> saveProxyConfiguration(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<>(configurationService.saveProxyConfiguration(payload), HttpStatus.OK);
    }

    @Feature(feature = CommonConstants.Features.PROXY_SETTINGS)
    @GetMapping("proxy/config")
    public ResponseEntity<Map<String, Object>> getProxyConfiguration() {
        return new ResponseEntity<>(configurationService.getProxyConfiguration(), HttpStatus.OK);
    }

    @Feature(feature = CommonConstants.Features.STORAGE_SETTINGS)
    @GetMapping("storage/statistics")
    public ResponseEntity<Map<String, Object>> getStorageStatistics() {
        return new ResponseEntity<>(configurationService.getStorageStatistics(), HttpStatus.OK);
    }

}

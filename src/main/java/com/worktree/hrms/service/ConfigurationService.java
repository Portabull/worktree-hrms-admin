package com.worktree.hrms.service;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface ConfigurationService {

    Map<String, Object> saveEmailConfiguration(@RequestBody Map<String, Object> payload);

    Map<String, Object> getEmailConfiguration();

    Map<String, Object> saveAIConfiguration(@RequestBody Map<String, Object> payload);

    Map<String, Object> getAIConfiguration();

    Map<String, Object> saveMobileConfiguration(Map<String, Object> payload);

    Map<String, Object> getMobileConfiguration();
}
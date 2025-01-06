package com.worktree.hrms.dao;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface ConfigurationDao {

    Map<String, Object> saveEmailConfiguration(@RequestBody Map<String, Object> payload);

    Map<String, Object> getEmailConfiguration();

    Map<String, Object> saveAIConfiguration(@RequestBody Map<String, Object> payload);

    Map<String, Object> getAIConfiguration();

    Map<String, Object> saveMobileConfiguration(Map<String, Object> payload);

    Map<String, Object> getMobileConfiguration();

    Map<String, Object> saveStorageConfiguration(Map<String, Object> payload);

    Map<String, Object> getStorageConfiguration();

    Map<String, Object> saveProxyConfiguration(Map<String, Object> payload);

    Map<String, Object> getProxyConfiguration();
}

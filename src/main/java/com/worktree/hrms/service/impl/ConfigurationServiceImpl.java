package com.worktree.hrms.service.impl;

import com.worktree.hrms.dao.ConfigurationDao;
import com.worktree.hrms.service.ConfigurationService;
import com.worktree.hrms.service.TestConfigService;
import com.worktree.hrms.utils.ConfigurationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationDao configurationDao;

    private final TestConfigService testConfigService;

    private final ConfigurationUtils configurationUtils;

    @Override
    public Map<String, Object> saveEmailConfiguration(Map<String, Object> payload) {
        testConfigService.validateEmailConfiguration(payload);
        return configurationDao.saveEmailConfiguration(payload);
    }

    @Override
    public Map<String, Object> getEmailConfiguration() {
        return configurationDao.getEmailConfiguration();
    }

    @Override
    public Map<String, Object> saveAIConfiguration(Map<String, Object> payload) {
        testConfigService.validateAIConfiguration(payload);
        return configurationDao.saveAIConfiguration(payload);
    }

    @Override
    public Map<String, Object> getAIConfiguration() {
        return configurationDao.getAIConfiguration();
    }

    @Override
    public Map<String, Object> saveMobileConfiguration(Map<String, Object> payload) {
        return configurationDao.saveMobileConfiguration(payload);
    }

    @Override
    public Map<String, Object> getMobileConfiguration() {
        return configurationDao.getMobileConfiguration();
    }

    @Override
    public Map<String, Object> saveStorageConfiguration(Map<String, Object> payload) {
        testConfigService.validateStorageConfiguration(payload);
        return configurationDao.saveStorageConfiguration(payload);
    }

    @Override
    public Map<String, Object> getStorageConfiguration() {
        return configurationDao.getStorageConfiguration();
    }

    @Override
    public Map<String, Object> saveProxyConfiguration(Map<String, Object> payload) {
        testConfigService.validateProxyConfiguration(payload);
        return configurationDao.saveProxyConfiguration(payload);
    }

    @Override
    public Map<String, Object> getProxyConfiguration() {
        return configurationDao.getProxyConfiguration();
    }

    @Override
    public Map<String, Object> getStorageStatistics() {
        Map<String, Object> storageConfiguration = configurationDao.getStorageConfiguration();
        return configurationUtils.getStorageStatistics(storageConfiguration);
    }
}

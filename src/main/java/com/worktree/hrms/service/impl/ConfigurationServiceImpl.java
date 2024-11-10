package com.worktree.hrms.service.impl;

import com.worktree.hrms.dao.ConfigurationDao;
import com.worktree.hrms.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private ConfigurationDao configurationDao;

    @Override
    public Map<String, Object> saveEmailConfiguration(Map<String, Object> payload) {
        return configurationDao.saveEmailConfiguration(payload);
    }

    @Override
    public Map<String, Object> getEmailConfiguration() {
        return configurationDao.getEmailConfiguration();
    }

    @Override
    public Map<String, Object> saveAIConfiguration(Map<String, Object> payload) {
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
}

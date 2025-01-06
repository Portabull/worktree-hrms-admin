package com.worktree.hrms.service;

import java.util.Map;

public interface TestConfigService {

    void validateEmailConfiguration(Map<String, Object> payload);

    void validateStorageConfiguration(Map<String, Object> payload);

    void validateAIConfiguration(Map<String, Object> payload);

    void validateProxyConfiguration(Map<String, Object> payload);
}

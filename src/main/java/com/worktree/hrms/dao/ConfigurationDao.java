package com.worktree.hrms.dao;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface ConfigurationDao {

    Map<String, Object> saveEmailConfiguration(@RequestBody Map<String, Object> payload);

    Map<String, Object> getEmailConfiguration();

    Map<String, Object> saveAIConfiguration(@RequestBody Map<String, Object> payload);

    Map<String, Object> getAIConfiguration();

}

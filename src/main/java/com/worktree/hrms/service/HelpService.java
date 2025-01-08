package com.worktree.hrms.service;

import java.util.List;
import java.util.Map;

public interface HelpService {

    List<Map<String, Object>> getHelpConfig();

    Map<String, Object> saveHelpConfig(Map<String, Object> payload);

    Map<String, Object> getHelpConfig(Long hid);

    Map<String, Object> getHelpConfig(String name);

    Map<String, Object> reset();
}

package com.worktree.hrms.dao;

import java.util.List;
import java.util.Map;

public interface HelpDao {

    List<Map<String, Object>> getHelpConfig();

    Map<String, Object> saveHelpConfig(Map<String, Object> payload);

    Map<String, Object> getHelpConfig(Long hid);

    Map<String, Object> getHelpConfig(String name);

    Map<String, Object> reset();
}

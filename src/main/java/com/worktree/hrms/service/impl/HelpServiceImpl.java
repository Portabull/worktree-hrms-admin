package com.worktree.hrms.service.impl;

import com.worktree.hrms.dao.HelpDao;
import com.worktree.hrms.service.HelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HelpServiceImpl implements HelpService {

    @Autowired
    HelpDao helpDao;

    @Override
    public List<Map<String, Object>> getHelpConfig() {
        return helpDao.getHelpConfig();
    }

    @Override
    public Map<String, Object> saveHelpConfig(Map<String, Object> payload) {
        return helpDao.saveHelpConfig(payload);
    }

    @Override
    public Map<String, Object> getHelpConfig(Long hid) {
        return helpDao.getHelpConfig(hid);
    }

    @Override
    public Map<String, Object> getHelpConfig(String name) {
        return helpDao.getHelpConfig(name);
    }

    @Override
    public Map<String, Object> reset() {
        return helpDao.reset();
    }
}

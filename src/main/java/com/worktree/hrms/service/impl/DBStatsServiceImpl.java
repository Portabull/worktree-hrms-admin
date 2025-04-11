package com.worktree.hrms.service.impl;

import com.worktree.hrms.dao.DBStatsDao;
import com.worktree.hrms.service.DBStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class DBStatsServiceImpl implements DBStatsService {


    @Autowired
    private DBStatsDao dbStatsDao;

    @Override
    public Map<String, Object> getDBStats(String type, Optional<String> schemaName, Optional<String> tableName) {
        return dbStatsDao.getDBStats(type, schemaName, tableName);
    }
}

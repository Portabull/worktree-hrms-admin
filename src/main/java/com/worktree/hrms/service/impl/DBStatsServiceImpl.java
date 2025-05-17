package com.worktree.hrms.service.impl;

import com.worktree.hrms.dao.DBStatsDao;
import com.worktree.hrms.service.DBStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DBStatsServiceImpl implements DBStatsService {

    private final DBStatsDao dbStatsDao;

    @Override
    public Map<String, Object> getDBStats(String type, Optional<String> schemaName, Optional<String> tableName) {
        return dbStatsDao.getDBStats(type, schemaName, tableName);
    }

    @Override
    public Map<String, Object> getDBDashboard() {
        return dbStatsDao.getStats();
    }
}

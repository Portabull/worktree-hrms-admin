package com.worktree.hrms.service.impl;

import com.worktree.hrms.dao.TenantDao;
import com.worktree.hrms.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantDao tenantDao;

    @Override
    public Map<String, Object> getTenants() {
        return tenantDao.getTenants();
    }
}

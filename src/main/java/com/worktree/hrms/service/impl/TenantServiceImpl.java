package com.worktree.hrms.service.impl;

import com.worktree.hrms.dao.TenantDao;
import com.worktree.hrms.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantDao tenantDao;

    @Override
    public Map<String, Object> getTenants() {
        return tenantDao.getTenants();
    }
}

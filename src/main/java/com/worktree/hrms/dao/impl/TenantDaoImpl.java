package com.worktree.hrms.dao.impl;

import com.worktree.hrms.dao.TenantDao;
import com.worktree.hrms.entity.BillingEntity;
import com.worktree.hrms.entity.TenantEntity;
import com.worktree.hrms.utils.HibernateUtils;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@RequiredArgsConstructor
public class TenantDaoImpl implements TenantDao {

    private final HibernateUtils hibernateUtils;

    @Override
    public Map<String, Object> getTenants() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> tenants = new ArrayList<>();
        List<Object> tenantsWithBillings;
        try (Session session = hibernateUtils.getSession()) {
            tenantsWithBillings =
                    session.createQuery("SELECT te,be FROM TenantEntity te join BillingEntity be on (te.tenantId=be.tenantId) WHERE be.isBillingActive=true").list();
        }
        AtomicInteger sNo = new AtomicInteger(1);
        tenantsWithBillings.forEach(tenantWithBilling -> {
            TenantEntity tenantEntity = (TenantEntity) ((Object[]) tenantWithBilling)[0];
            BillingEntity billingEntity = (BillingEntity) ((Object[]) tenantWithBilling)[1];
            Map<String, Object> tenant = new HashMap<>();
            tenant.put("sNo", sNo.getAndIncrement());
            tenant.put("tenantId", tenantEntity.getTenantId());
            tenant.put("tenantName", tenantEntity.getTenantName());
            tenant.put("tenantUsers", tenantEntity.getTenantUsers());
            tenant.put("billingEndDate", billingEntity.getLicenceExpiryDate());
            tenant.put("createdDate", tenantEntity.getTenantCreatedDate());
            tenants.add(tenant);
        });
        response.put("tenants", tenants);
        return response;
    }
}

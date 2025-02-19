package com.worktree.hrms.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.dao.LicenceDao;
import com.worktree.hrms.dao.UserDao;
import com.worktree.hrms.entity.Licence;
import com.worktree.hrms.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LicenceDaoImpl implements LicenceDao {

    @Autowired
    private HibernateUtils hibernateUtils;

    @Autowired
    private UserDao userDao;

    @Override
    public synchronized Map<String, Object> uploadLicence(String licence) {
        userDao.isAdminUser();
        Transaction transaction = null;
        try (Session session = hibernateUtils.getSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM Licence").executeUpdate();
            session.createQuery("UPDATE UserTokenEntity SET licenseVerified=true").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

        hibernateUtils.saveOrUpdateEntity(new Licence(licence));

        return CommonConstants.SUCCESS_RESPONSE;
    }

    @Override
    public Map<String, Object> getLicence() {
        Map<String, Object> response = new HashMap<>();
        userDao.isAdminUser();
        try (Session session = hibernateUtils.getSession()) {
            List<String> licences = session.createQuery("SELECT licence FROM Licence").list();
            if (!CollectionUtils.isEmpty(licences)) {
                Map<String, Object> licenseResponse = new ObjectMapper().readValue(licences.get(0), Map.class);
                response = Map.of(
                        "companyName", licenseResponse.getOrDefault("companyName",""),
                        "companyEmail", licenseResponse.getOrDefault("companyEmail",""),
                        "startDate", licenseResponse.getOrDefault("licenseStartedDate",""),
                        "expiryDate", licenseResponse.getOrDefault("licenseValidTill",""),
                        "nextRegDate", "",
                        "noOfTenants", licenseResponse.getOrDefault("noOfTenants",""),
                        "maxUsersPerTenant", licenseResponse.getOrDefault("noOfTenantMaxUsersCount","")
                );
            }
        } catch (Exception e) {
            System.out.println();
            e.printStackTrace();
        }
        return response;
    }

}

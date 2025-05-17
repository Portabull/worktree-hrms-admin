package com.worktree.hrms.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.dao.LicenceDao;
import com.worktree.hrms.dao.UserDao;
import com.worktree.hrms.entity.Licence;
import com.worktree.hrms.utils.DateUtils;
import com.worktree.hrms.utils.HibernateUtils;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class LicenceDaoImpl implements LicenceDao {

    private final HibernateUtils hibernateUtils;

    private static final Logger log = LoggerFactory.getLogger(LicenceDaoImpl.class);

    private final UserDao userDao;

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
                        "companyName", licenseResponse.getOrDefault("companyName", ""),
                        "companyEmail", licenseResponse.getOrDefault("companyEmail", ""),
                        "startDate", licenseResponse.getOrDefault("licenseStartedDate", ""),
                        "expiryDate", licenseResponse.getOrDefault("licenseValidTill", ""),
                        "nextRegDate", getNextRegDate(licenseResponse.getOrDefault("licenseValidTill", "").toString()),
                        "noOfTenants", licenseResponse.getOrDefault("noOfTenants", ""),
                        "maxUsersPerTenant", licenseResponse.getOrDefault("noOfTenantMaxUsersCount", "")
                );
            }
        } catch (Exception e) {
            log.error("Exception ::", e);
        }
        return response;
    }

    public String getNextRegDate(String expiryDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.LICENSE_DATE_FORMAT);
        LocalDate date = LocalDate.parse(expiryDate, formatter);
        LocalDate newDate = date.plusDays(1);
        return newDate.format(formatter);
    }

}

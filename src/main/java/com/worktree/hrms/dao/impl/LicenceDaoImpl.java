package com.worktree.hrms.dao.impl;

import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.dao.LicenceDao;
import com.worktree.hrms.entity.Licence;
import com.worktree.hrms.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class LicenceDaoImpl implements LicenceDao {

    @Autowired
    private HibernateUtils hibernateUtils;

    @Override
    public synchronized Map<String, Object> uploadLicence(String licence) {
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

}

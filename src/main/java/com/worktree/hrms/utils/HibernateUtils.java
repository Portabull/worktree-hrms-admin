package com.worktree.hrms.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class HibernateUtils {

    @Autowired
    private SessionFactory sessionFactory;

    public Session getSession() {
        if (this.sessionFactory != null) {
            return this.sessionFactory.openSession();
        }
        return null;
    }

    public <T> T saveOrUpdateEntity(T entity) {
        try (Session session = getSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.saveOrUpdate(entity);
                transaction.commit();
                return entity;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    public <T> T findEntity(Class<?> entityClass, Serializable primaryId) {
        try (Session session = this.getSession()) {
            return (T) session.get(entityClass, primaryId);
        }
    }


}

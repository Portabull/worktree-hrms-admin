package com.worktree.hrms.utils;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HibernateUtils {

    private final SessionFactory sessionFactory;

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

    public <T> List<T> findEntitiesByCriteria(Class<T> entityClass, String primaryPropertyName, Serializable primaryId) {
        try (Session session = getSession()) {
            String queryBuilder = new StringBuilder("FROM ").append(entityClass.getCanonicalName())
                    .append(" WHERE ").append(primaryPropertyName).append(" = :primaryPropertyName").toString();
            return (List<T>) session.createQuery(queryBuilder, entityClass)
                    .setParameter("primaryPropertyName", primaryId).list();
        }
    }

    public <T> T findEntityByCriteria(Class<T> entityClass, String primaryPropertyName, Serializable primaryId) {
        try (Session session = getSession()) {
            String queryBuilder = new StringBuilder("FROM ").append(entityClass.getCanonicalName())
                    .append(" WHERE ").append(primaryPropertyName).append(" = :primaryPropertyName").toString();
            return (T) session.createQuery(queryBuilder, entityClass)
                    .setParameter("primaryPropertyName", primaryId).uniqueResult();
        }
    }


}

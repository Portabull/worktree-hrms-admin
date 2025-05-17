package com.worktree.hrms.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worktree.hrms.config.TestConfig;
import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.dao.HelpDao;
import com.worktree.hrms.dao.UserDao;
import com.worktree.hrms.entity.HelpEntity;
import com.worktree.hrms.exceptions.BadRequestException;
import com.worktree.hrms.utils.DateUtils;
import com.worktree.hrms.utils.HibernateUtils;
import jakarta.persistence.RollbackException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class HelpDaoImpl implements HelpDao {

    private final HibernateUtils hibernateUtils;

    private final UserDao userDao;

    private final DateUtils dateUtils;

    private final ObjectMapper objectMapper;

    private final TestConfig testConfig;

    @Override
    public List<Map<String, Object>> getHelpConfig() {
        userDao.isAdminUser();

        List<Map<String, Object>> response = new ArrayList<>();
        try (Session session = hibernateUtils.getSession()) {
            List<Object[]> dbResponse = session.createQuery("SELECT ue.displayName,he.helpId,he.helpConfigName,he.createdDate,he.updatedDate FROM HelpEntity he LEFT JOIN UserEntity ue on (he.updatedBy=ue.userID)").list();

            if (!CollectionUtils.isEmpty(dbResponse)) {
                dbResponse.forEach(resp -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("updatedBy", resp[0] != null ? resp[0].toString() : "N/A");
                    data.put("id", resp[1]);
                    data.put("title", resp[2]);

                    data.put("createdAt", dateUtils.format(DateUtils.DD_MM_YYYY_HH_MM, (Date) resp[3]));
                    data.put("updatedAt", dateUtils.format(DateUtils.DD_MM_YYYY_HH_MM, (Date) resp[4]));
                    response.add(data);
                });
            }
        }
        return response;
    }

    @Override
    public Map<String, Object> saveHelpConfig(Map<String, Object> payload) {
        userDao.isAdminUser();
        try {
            Long userId = userDao.getCurrentLoggedInUserId();
            Date date = dateUtils.getCurrentDate();
            HelpEntity helpEntity = null;
            if (payload.get("id") != null) {
                helpEntity = hibernateUtils.findEntity(HelpEntity.class, Long.valueOf(payload.get("id").toString()));
            }

            if (helpEntity == null) {
                helpEntity = new HelpEntity();
                helpEntity.setCreatedBy(userId);
                helpEntity.setCreatedDate(date);
            }

            helpEntity.setUpdatedBy(userId);
            helpEntity.setUpdatedDate(date);
            helpEntity.setHelpConfigName(payload.get("title").toString());
            helpEntity.setHelpConfiguration(payload.get("configuration").toString());

            hibernateUtils.saveOrUpdateEntity(helpEntity);

            return Map.of("id", helpEntity.getHelpId());

        } catch (RollbackException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new BadRequestException("Already Main bar header present please try a different and unique one");
            }
        }

        return CommonConstants.SUCCESS_RESPONSE;
    }

    @Override
    public Map<String, Object> getHelpConfig(Long hid) {
        try {
            HelpEntity helpEntity = hibernateUtils.findEntity(HelpEntity.class, hid);
            return objectMapper.readValue(helpEntity.getHelpConfiguration(), Map.class);
        } catch (Exception e) {
            throw new BadRequestException(CommonConstants.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Map<String, Object> getHelpConfig(String name) {
        try {
            name = new String(Base64.getDecoder().decode(name));
            HelpEntity helpEntity = hibernateUtils.findEntityByCriteria(HelpEntity.class, "helpConfigName", name);
            return objectMapper.readValue(helpEntity.getHelpConfiguration(), Map.class);
        } catch (Exception e) {
            throw new BadRequestException(CommonConstants.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Map<String, Object> reset() {
        userDao.isAdminUser();
        Transaction transaction = null;
        try (Session session = hibernateUtils.getSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM HelpEntity").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

        testConfig.loadInitHelp();

        return CommonConstants.SUCCESS_RESPONSE;
    }

}

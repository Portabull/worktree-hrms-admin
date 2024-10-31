package com.worktree.hrms.dao.impl;

import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.dao.UserDao;
import com.worktree.hrms.dao.UserProfileSettingsDao;
import com.worktree.hrms.entity.UserEntity;
import com.worktree.hrms.entity.UserTokenEntity;
import com.worktree.hrms.utils.HibernateUtils;
import com.worktree.hrms.utils.RequestHelper;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserProfileSettingsDaoImpl implements UserProfileSettingsDao {

    @Autowired
    private HibernateUtils hibernateUtils;

    @Autowired
    private UserDao userDao;

    @Override
    public Map<String, Object> settings() {
        Map<String, Object> response = new HashMap<>();
        UserEntity userEntity;
        List<UserTokenEntity> userTokens = null;
        String currentToken = RequestHelper.getAuthorizationToken();
        try (Session session = hibernateUtils.getSession()) {
            userEntity = (UserEntity) session.createQuery(" FROM UserEntity WHERE userID = (SELECT userID FROM UserTokenEntity WHERE jwt = :token)")
                    .setParameter("token", currentToken).uniqueResult();
            if (userEntity != null)
                userTokens = session.createQuery("FROM UserTokenEntity WHERE userID=:userID").setParameter("userID", userEntity.getUserID()).list();
        }

        if (userEntity != null) {
            Map<String, Object> profile = new HashMap<>();
            profile.put("name", userEntity.getDisplayName());
            profile.put("mobile", userEntity.getMobileNumber());
            profile.put("email", userEntity.getEmail());
            profile.put("location", userEntity.getLocation());
            profile.put("alternativeMobileNumber", userEntity.getAlternativeMobileNumber());
            profile.put("Address", userEntity.getAddress());
            response.put("profile", profile);
            response.put("viewAccounts", userEntity.isAdmin() != null && userEntity.isAdmin());
        }

        List<Map<String, Object>> userSettings = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userTokens)) {
            userTokens.forEach(userToken -> {
                Map<String, Object> settings = new HashMap<>();
                settings.put("loggedInTime", "");
                settings.put("loggedInDevice", "");
                settings.put("loginLocation", "https://google.com");
                settings.put("tokenId", userToken.getTokenId());
                settings.put("currentUser", userToken.getJwt().equalsIgnoreCase(currentToken));
                userSettings.add(settings);
            });
        }

        response.put("security", userSettings);
        return response;
    }

    @Override
    public Map<String, Object> saveProfile(Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        UserEntity userEntity;
        String currentToken = RequestHelper.getAuthorizationToken();
        try (Session session = hibernateUtils.getSession()) {
            userEntity = (UserEntity) session.createQuery(" FROM UserEntity WHERE userID = (SELECT userID FROM UserTokenEntity WHERE jwt = :token)")
                    .setParameter("token", currentToken).uniqueResult();
        }

        if (userEntity != null) {
            userEntity.setDisplayName(String.valueOf(payload.get("userName")));
            userEntity.setMobileNumber(String.valueOf(payload.get("mobile")));
            userEntity.setEmail(String.valueOf(payload.get("email")));
            userEntity.setLocation(String.valueOf(payload.get("location")));
            userEntity.setAlternativeMobileNumber(String.valueOf(payload.get("altMobile")));
            userEntity.setAddress(String.valueOf(payload.get("address")));
            hibernateUtils.saveOrUpdateEntity(userEntity);
        }

        response.put(CommonConstants.STATUS, CommonConstants.SUCCESS);
        response.put(CommonConstants.STATUS_CODE, 200);

        return response;
    }

    @Override
    public Map<String, Object> profileLogout(Long tokenId) {
        Map<String, Object> response = new HashMap<>();
        UserEntity userEntity;
        UserTokenEntity userTokenEntity;
        String currentToken = RequestHelper.getAuthorizationToken();
        try (Session session = hibernateUtils.getSession()) {
            userEntity = (UserEntity) session.createQuery(" FROM UserEntity WHERE userID = (SELECT userID FROM UserTokenEntity WHERE jwt = :token)")
                    .setParameter("token", currentToken).uniqueResult();
            if (userEntity != null) {
                userTokenEntity = (UserTokenEntity) session.createQuery("FROM UserTokenEntity WHERE tokenId = :tokenId").
                        setParameter("tokenId", tokenId).uniqueResult();
                if (userTokenEntity != null && userTokenEntity.getUserID() == userEntity.getUserID()) {
                    return userDao.logout(userTokenEntity.getJwt());
                }
            }
        }

        return response;
    }


    @Override
    public Map<String, Object> uploadProfiePic(String profilePic) {
        Map<String, Object> response = new HashMap<>();
        String currentToken = RequestHelper.getAuthorizationToken();

        Transaction transaction = null;
        try (Session session = hibernateUtils.getSession()) {
            // Begin transaction
            transaction = session.beginTransaction();

            // Update query
            int updatedRows = session.createQuery(
                            "UPDATE UserEntity SET profilePic = :profilePic WHERE userID = (SELECT userID FROM UserTokenEntity WHERE jwt = :token)")
                    .setParameter("token", currentToken)
                    .setParameter("profilePic", profilePic)
                    .executeUpdate();

            // Commit transaction
            transaction.commit();

            // Return a response
            response.put("status", "success");
            response.put("updatedRows", updatedRows);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Roll back on error
            }
            response.put("status", "error");
            response.put("message", e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

}

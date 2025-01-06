package com.worktree.hrms.dao.impl;

import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.dao.UserDao;
import com.worktree.hrms.entity.FeatureEntity;
import com.worktree.hrms.entity.UserEntity;
import com.worktree.hrms.entity.UserFeatures;
import com.worktree.hrms.entity.UserTokenEntity;
import com.worktree.hrms.exceptions.BadRequestException;
import com.worktree.hrms.exceptions.ForbiddenException;
import com.worktree.hrms.utils.DateUtils;
import com.worktree.hrms.utils.HibernateUtils;
import com.worktree.hrms.utils.RequestHelper;
import jakarta.persistence.RollbackException;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private HibernateUtils hibernateUtils;
    @Autowired
    private DateUtils dateUtils;

    @Override
    public Long existsUserNamePassword(String userName, String password) {
        try (Session session = hibernateUtils.getSession()) {
            return (Long) session.createQuery("SELECT userID FROM UserEntity WHERE userName=:userName and password=:password")
                    .setParameter("password", password).setParameter("userName", userName).uniqueResult();
        }
    }

    @Override
    public void saveRandomToken(Long userId, String jwt, boolean licenseVerified) {
        UserTokenEntity entity = new UserTokenEntity();
        entity.setJwt(jwt);
        entity.setUserID(userId);
        entity.setDate(dateUtils.getCurrentDate());
        entity.setLocation(RequestHelper.getHeader("latlong"));
        entity.setDeviceDetails(RequestHelper.getHeader("dd"));
        entity.setLicenseVerified(licenseVerified);
        hibernateUtils.saveOrUpdateEntity(entity);
    }

    @Override
    public Map<String, String> getUserProfileInfo(String userName) {
        Map<String, String> response = new HashMap<>();
        Object[] profileResponse;
        try (Session session = hibernateUtils.getSession()) {
            profileResponse = (Object[]) session.createQuery("SELECT displayName,profilePic FROM UserEntity WHERE userName =:userName")
                    .setParameter("userName", userName).uniqueResult();
        }
        response.put("displayName", profileResponse[0] != null ? profileResponse[0].toString() : null);
        response.put("profilePic", profileResponse[1] != null ? profileResponse[1].toString() : null);
        return response;

    }

    @Transactional
    @Override
    public Map<String, Object> logout() {
        String authorizationToken = RequestHelper.getAuthorizationToken();
        return logout(authorizationToken);
    }

    @Override
    public Map<String, Object> logout(String authorizationToken) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("statusCode", 200);

        System.out.println(authorizationToken);
        List<String> authorizationTokens;
        if (authorizationToken.contains(",")) {
            authorizationTokens = Arrays.stream(authorizationToken.split(","))
                    .map(String::trim) // Optional: To trim spaces
                    .collect(Collectors.toList());
        } else {
            authorizationTokens = Arrays.asList(authorizationToken);
        }
        Transaction transaction = null;
        try (Session session = hibernateUtils.getSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM UserTokenEntity WHERE jwt IN (:token)")
                    .setParameter("token", authorizationTokens).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return response;
    }

    @Override
    public Map<String, Object> getUsers(Integer page, String searchText) {
        int fetchSize = 25;
        if (page == null) {
            page = 0;
        }

        int firstResult = page * fetchSize;

        Map<String, Object> usersResponse = new HashMap<>();

        List<UserEntity> users;
        List<FeatureEntity> features;
        List<Map<String, Object>> response = new ArrayList<>();
        if (!isAdminUser()) {
            throw new ForbiddenException(CommonConstants.ACCESS_DENIED);
        }

        StringBuilder queryBuilder = new StringBuilder("FROM UserEntity");

        if (searchText != null && !searchText.trim().isEmpty()) {
            queryBuilder.append(" WHERE userName LIKE :searchText");
        }

        try (Session session = hibernateUtils.getSession()) {
            Query query = session.createQuery(queryBuilder.toString());

            if (searchText != null && !searchText.trim().isEmpty()) {
                // Set the searchText parameter
                query.setParameter("searchText", "%" + searchText + "%");
            }

            users = query.setFirstResult(firstResult) // Set the offset
                    .setMaxResults(fetchSize)    // Set the fetch size
                    .list();
            features = session.createQuery("FROM FeatureEntity")
                    .list();
        }

        if (!CollectionUtils.isEmpty(users)) {
            AtomicInteger sNo = new AtomicInteger(firstResult + 1);
            users.forEach(user -> {
                Map<String, Object> userResponse = new HashMap<>();
                userResponse.put("sNo", sNo.getAndIncrement());
                userResponse.put("userName", user.getUserName());
                userResponse.put("displayName", user.getDisplayName());
                userResponse.put("email", user.getEmail());
                userResponse.put("mobile", user.getMobileNumber());
                userResponse.put("isAdmin", user.isAdmin() != null && user.isAdmin());
                userResponse.put("userId", user.getUserID());
                try (Session session = hibernateUtils.getSession()) {
                    userResponse.put("userFeatures",
                            session.createQuery("SELECT fe.featureName FROM UserFeatures uf JOIN FeatureEntity fe on (uf.featureId=fe.featureId) WHERE uf.userID =:userID")
                                    .setParameter("userID", user.getUserID()).list());
                }
                response.add(userResponse);
            });
        }


        usersResponse.put("maxPageSize", fetchSize);
        usersResponse.put("users", response);
        usersResponse.put("features", features.stream().map(FeatureEntity::getFeatureName).collect(Collectors.toList()));
        return usersResponse;

    }

    @Override
    public Map<String, Object> saveUser(Map<String, Object> payload) {

        try {

            if (!isAdminUser()) {
                throw new ForbiddenException(CommonConstants.ACCESS_DENIED);
            }

            UserEntity userEntity;
            try {
                userEntity = hibernateUtils.findEntity(UserEntity.class, Long.valueOf(String.valueOf(payload.get("userId"))));
            } catch (NumberFormatException e) {
                userEntity = new UserEntity();
            }

            validateAdminDetails(userEntity, payload);

            userEntity.setUserName(String.valueOf(payload.get("userName")));
            userEntity.setDisplayName(String.valueOf(payload.get("displayName")));
            userEntity.setEmail(String.valueOf(payload.get("email")));
            userEntity.setMobileNumber(String.valueOf(payload.get("mobile")));
            userEntity.setAdmin(String.valueOf(payload.get("isAdmin")).equalsIgnoreCase("true"));
            if (payload.get("password") != null && payload.get("password").toString().trim().length() != 0) {
                userEntity.setPassword(payload.get("password").toString());
            }

            hibernateUtils.saveOrUpdateEntity(userEntity);

            List<FeatureEntity> features = null;

            Transaction transaction = null;
            try (Session session = hibernateUtils.getSession()) {

                if (payload.get("userId") != null) {

                    transaction = session.beginTransaction();

                    session.createQuery("DELETE FROM UserFeatures WHERE userID = :userID")
                            .setParameter("userID", Long.valueOf(String.valueOf(payload.get("userId"))))
                            .executeUpdate();

                    transaction.commit();

                }

                if (!CollectionUtils.isEmpty((List) payload.get("userFeatures"))) {
                    features = (List<FeatureEntity>) session.createQuery("FROM FeatureEntity WHERE featureName IN (:featureNames)")
                            .setParameter("featureNames", payload.get("userFeatures")).list();
                }

            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }

            if (!CollectionUtils.isEmpty(features)) {
                for (FeatureEntity feature : features) {
                    UserFeatures userFeature = new UserFeatures();
                    userFeature.setUserID(payload.get("userId") != null ? Long.valueOf(String.valueOf(payload.get("userId"))) : userEntity.getUserID());
                    userFeature.setFeatureId(feature.getFeatureId());
                    hibernateUtils.saveOrUpdateEntity(userFeature);
                }
            }
        } catch (RollbackException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new BadRequestException("Already username exists please try with different username");
            }
        }

        return CommonConstants.SUCCESS_RESPONSE;
    }

    private void validateAdminDetails(UserEntity userEntity, Map<String, Object> payload) {

        if ("admin".equalsIgnoreCase(userEntity.getUserName())) {
            if (!String.valueOf(payload.get("userName")).equalsIgnoreCase(userEntity.getUserName())) {
                throw new BadRequestException("You cannot change the display name or username for admin");
            }

            if (!String.valueOf(payload.get("displayName")).equalsIgnoreCase(userEntity.getDisplayName())) {
                throw new BadRequestException("You cannot change the display name or username for admin");
            }

            if (String.valueOf(payload.get("isAdmin")).equalsIgnoreCase("false")) {
                throw new BadRequestException("You cannot change the admin roles");
            }
        }
    }

    @Override
    public boolean isAdminUser() {
        String currentToken = RequestHelper.getAuthorizationToken();
        UserEntity userEntity;
        try (Session session = hibernateUtils.getSession()) {
            userEntity = (UserEntity) session.createQuery("FROM UserEntity WHERE userID = (SELECT userID FROM UserTokenEntity WHERE jwt = :token)")
                    .setParameter("token", currentToken).uniqueResult();
            return userEntity.isAdmin() != null && userEntity.isAdmin();
        }
    }

    @Override
    public Map<String, Object> deleteUser(Long userId) {

        if (!isAdminUser()) {
            throw new ForbiddenException(CommonConstants.ACCESS_DENIED);
        }

        UserEntity userEntity = hibernateUtils.findEntity(UserEntity.class, userId);

        if ("admin".equalsIgnoreCase(userEntity.getUserName())) {
            throw new BadRequestException("You cannot delete admin user");
        }


        Transaction transaction = null;
        List<UserTokenEntity> userTokens;
        try (Session session = hibernateUtils.getSession()) {
            transaction = session.beginTransaction();

            session.createQuery("DELETE FROM UserEntity WHERE userID = :userID AND userName != 'admin'")
                    .setParameter("userID", userId).executeUpdate();

            userTokens = session.createQuery(" FROM UserTokenEntity WHERE userID = :userID")
                    .setParameter("userID", userId).list();

            // Commit transaction
            transaction.commit();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            if (transaction != null) {
                transaction.rollback(); // Roll back on error
            }
            response.put("status", "error");
            response.put("message", e.getMessage());
            return response;
        }

        if (!CollectionUtils.isEmpty(userTokens)) {
            userTokens.forEach(userToken -> {
                logout(userToken.getJwt());
            });
        }

        return CommonConstants.SUCCESS_RESPONSE;
    }

    @Override
    public List<String> getFeatures(String userName) {
        try (Session session = hibernateUtils.getSession()) {
            return session.createQuery("SELECT fe.featureName FROM UserFeatures uf JOIN FeatureEntity fe on (uf.featureId=fe.featureId) WHERE uf.userID = (SELECT userID FROM UserEntity WHERE userName=:userName)")
                    .setParameter("userName", userName).list();
        }
    }

}

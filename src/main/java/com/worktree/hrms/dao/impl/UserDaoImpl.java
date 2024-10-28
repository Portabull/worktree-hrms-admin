package com.worktree.hrms.dao.impl;

import com.worktree.hrms.dao.UserDao;
import com.worktree.hrms.entity.UserTokenEntity;
import com.worktree.hrms.utils.HibernateUtils;
import com.worktree.hrms.utils.RequestHelper;
import com.worktree.hrms.utils.TokenFileUtils;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class UserDaoImpl implements UserDao {


    @Autowired
    private HibernateUtils hibernateUtils;

    @Autowired
    private TokenFileUtils tokenFileUtils;

    @Override
    public Long existsUserNamePassword(String userName, String password) {
        try (Session session = hibernateUtils.getSession()) {
            return (Long) session.createQuery("SELECT userID FROM UserEntity WHERE userName=:userName and password=:password")
                    .setParameter("password", password).setParameter("userName", userName).uniqueResult();
        }
    }

    @Override
    public void saveRandomToken(Long userId, String jwt) {
        UserTokenEntity entity = new UserTokenEntity();
        entity.setJwt(jwt);
        entity.setUserID(userId);
        hibernateUtils.saveOrUpdateEntity(entity);
        tokenFileUtils.addTokenToFileCache(jwt);
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
            tokenFileUtils.removeTokenFromFileCache(authorizationToken);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return response;
    }


}

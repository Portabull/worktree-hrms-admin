package com.worktree.hrms.service.impl;

import com.worktree.hrms.dao.UserDao;
import com.worktree.hrms.service.LoginService;
import com.worktree.hrms.utils.EncyptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private EncyptionUtils encyptionUtils;

    @Override
    public Map<String, Object> login(Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String userName = payload.get("userName");
        String password = payload.get("password");
        String pass[] = password.split("::");
        password = encyptionUtils.decrypt(pass[1], pass[0], "NWIYRFIYF%@&#$)ABCDEFGHIJKLMNOP");
        Long userId = userDao.existsUserNamePassword(userName, password);
        if (userId != null) {
            response.put("status", "SUCCESS");
            String jwt = UUID.randomUUID() + "" + new Date().getTime();
            userDao.saveRandomToken(userId, jwt);
            response.put("jwt", jwt);
            Map<String, String> userProfileInfo = userDao.getUserProfileInfo(userName);
            response.put("userDisplayName", userProfileInfo.get("displayName"));
            response.put("userProfileImage", userProfileInfo.get("profilePic"));
            response.put("userFeatures", userDao.getFeatures(userName));
        } else {
            response.put("status", "FAILED");
            response.put("message", "Invalid Credentials");
        }
        return response;
    }

    @Override
    public Map<String, Object> logout() {
        return userDao.logout();
    }

}

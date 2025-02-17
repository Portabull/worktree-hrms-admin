package com.worktree.hrms.service.impl;

import com.worktree.hrms.config.TestConfig;
import com.worktree.hrms.dao.CommonDao;
import com.worktree.hrms.dao.UserDao;
import com.worktree.hrms.exceptions.PaymentRequiredException;
import com.worktree.hrms.service.LoginService;
import com.worktree.hrms.utils.EncryptionUtils;
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
    private CommonDao commonDao;

    @Override
    public Map<String, Object> login(Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String userName = payload.get("userName");
        String password = payload.get("password");
        password = EncryptionUtils.decrypt(password);
        Long userId = userDao.existsUserNamePassword(userName, password);
        if (userId != null) {
            boolean licenseVerified = true;
            try {
                commonDao.validLicense();
            } catch (PaymentRequiredException e) {
                response.put("statusCode", "402");
                licenseVerified = false;
            }

            response.put("status", "SUCCESS");
            response.put("licenseVerified", licenseVerified);
            String jwt = UUID.randomUUID() + "" + new Date().getTime();
            userDao.saveRandomToken(userId, jwt, licenseVerified);
            response.put("jwt", jwt);
            Map<String, String> userProfileInfo = userDao.getUserProfileInfo(userName);
            response.put("userDisplayName", userProfileInfo.get("displayName"));
            response.put("userProfileImage", userProfileInfo.get("profilePic") != null &&
                    !userProfileInfo.get("profilePic").trim().equals("") ? userProfileInfo.get("profilePic")
                    : TestConfig.DEFAULT_PROFILE_PIC);
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

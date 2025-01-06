package com.worktree.hrms.service.impl;

import com.worktree.hrms.dao.UserDao;
import com.worktree.hrms.dao.UserProfileSettingsDao;
import com.worktree.hrms.service.UserProfileSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserProfileSettingsServiceImpl implements UserProfileSettingsService {

    @Autowired
    private UserProfileSettingsDao userProfileSettingsDao;

    @Autowired
    private UserDao userDao;


    @Override
    public Map<String, Object> settings() {
        return userProfileSettingsDao.settings();
    }

    @Override
    public Map<String, Object> saveProfile(Map<String, Object> payload) {
        return userProfileSettingsDao.saveProfile(payload);
    }

    @Override
    public Map<String, Object> profileLogout(Long tokenId) {
        return userProfileSettingsDao.profileLogout(tokenId);
    }

    @Override
    public Map<String, Object> uploadProfiePic(String profilePic) {
        return userProfileSettingsDao.uploadProfiePic(profilePic);
    }

    @Override
    public Map<String, Object> getUsers(Integer page, String searchText) {
        return userDao.getUsers(page, searchText);
    }

    @Override
    public Map<String, Object> saveUser(Map<String, Object> payload) {
        return userDao.saveUser(payload);
    }

    @Override
    public Map<String, Object> deleteUser(Long userId) {
        return userDao.deleteUser(userId);
    }
}

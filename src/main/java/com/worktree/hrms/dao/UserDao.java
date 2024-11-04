package com.worktree.hrms.dao;

import java.util.List;
import java.util.Map;

public interface UserDao {

    Long existsUserNamePassword(String userName, String password);

    void saveRandomToken(Long userId, String jwt);

    public Map<String, String> getUserProfileInfo(String userName);

    Map<String, Object> logout();

    Map<String, Object> logout(String token);

    Map<String, Object> getUsers();

    Map<String, Object> saveUser(Map<String, Object> payload);

    boolean isAdminUser();

    Map<String, Object> deleteUser(Long userId);

    List<String> getFeatures(String userName);
}

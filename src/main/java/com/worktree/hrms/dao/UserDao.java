package com.worktree.hrms.dao;

import java.util.Map;

public interface UserDao {

    Long existsUserNamePassword(String userName, String password);

    void saveRandomToken(Long userId, String jwt);

    public Map<String, String> getUserProfileInfo(String userName);

    Map<String, Object> logout();

    Map<String, Object> logout(String token);
}

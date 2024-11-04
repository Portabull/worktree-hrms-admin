package com.worktree.hrms.service;

import java.util.Map;

public interface UserProfileSettingsService {

    Map<String, Object> settings();

    Map<String, Object> saveProfile(Map<String, Object> payload);

    Map<String, Object> profileLogout(Long tokenId);

    Map<String, Object> uploadProfiePic(String s);

    Map<String, Object> getUsers();

    Map<String, Object> saveUser(Map<String, Object> payload);

    Map<String, Object> deleteUser(Long userId);
}

package com.worktree.hrms.dao;

import java.util.Map;

public interface UserProfileSettingsDao {

    Map<String, Object> settings();

    Map<String, Object> saveProfile(Map<String, Object> payload);

    Map<String, Object> profileLogout(Long tokenId);

    Map<String, Object> uploadProfiePic(String profilePic);
}

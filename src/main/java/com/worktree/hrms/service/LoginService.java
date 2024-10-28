package com.worktree.hrms.service;

import java.util.Map;

public interface LoginService {

    Map<String, Object> login(Map<String, String> payload);

    Map<String, Object> logout();
}

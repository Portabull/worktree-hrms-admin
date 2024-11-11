package com.worktree.hrms.dao;

import java.util.Map;

public interface CommonDao {

    Long getLoggedInUserId();

    void userHasFeature(String feature);

    Map<String, Object> getCoupons();

    Map<String, Object> saveCoupon(Map<String, Object> payload);

}

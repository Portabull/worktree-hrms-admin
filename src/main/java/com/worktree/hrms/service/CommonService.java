package com.worktree.hrms.service;

import java.util.Map;

public interface CommonService {

    Map<String, Object> getCoupons();

    Map<String, Object> saveCoupon(Map<String, Object> payload);

}

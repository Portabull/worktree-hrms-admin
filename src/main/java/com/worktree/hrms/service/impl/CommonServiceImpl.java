package com.worktree.hrms.service.impl;

import com.worktree.hrms.dao.CommonDao;
import com.worktree.hrms.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> getCoupons() {
        return commonDao.getCoupons();
    }

    @Override
    public Map<String, Object> saveCoupon(Map<String, Object> payload) {
        return commonDao.saveCoupon(payload);
    }
}

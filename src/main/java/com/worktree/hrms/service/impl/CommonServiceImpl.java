package com.worktree.hrms.service.impl;

import com.worktree.hrms.dao.CommonDao;
import com.worktree.hrms.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final CommonDao commonDao;

    @Override
    public Map<String, Object> getCoupons() {
        return commonDao.getCoupons();
    }

    @Override
    public Map<String, Object> saveCoupon(Map<String, Object> payload) {
        return commonDao.saveCoupon(payload);
    }
}

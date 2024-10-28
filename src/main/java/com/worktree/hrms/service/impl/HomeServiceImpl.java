package com.worktree.hrms.service.impl;

import com.worktree.hrms.dao.HomeDao;
import com.worktree.hrms.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private HomeDao homeDao;

    @Override
    public Map<String, Object> home(String type) {
        return homeDao.home(type);
    }
}

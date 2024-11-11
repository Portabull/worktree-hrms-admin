package com.worktree.hrms.annotations;

import com.worktree.hrms.dao.CommonDao;
import com.worktree.hrms.utils.RequestHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FeatureAspect {

    @Autowired
    private CommonDao commonDao;

    @Around("@annotation(Feature)")
    public Object validateFeature(ProceedingJoinPoint joinPoint) throws Throwable {
        commonDao.userHasFeature(RequestHelper.getHeader("featureType"));
        return joinPoint.proceed();
    }

}

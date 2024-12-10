package com.worktree.hrms.config;

import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.entity.FeatureEntity;
import com.worktree.hrms.entity.UserEntity;
import com.worktree.hrms.utils.DateUtils;
import com.worktree.hrms.utils.HibernateUtils;
import jakarta.annotation.PostConstruct;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Autowired
    private DateUtils dateUtils;

    @Autowired
    private HibernateUtils hibernateUtils;

    public static final String DEFAULT_PROFILE_PIC = "data:image/jpeg;base64,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";

    @PostConstruct
    public void init() {
        UserEntity admin = getUserEntity("admin");
        if (admin == null) {
            admin = new UserEntity();
        }
        admin.setUserName("admin");
        admin.setPassword("admin");
        admin.setProfilePic(DEFAULT_PROFILE_PIC);
        admin.setDisplayName("Administrator");
        admin.setAdmin(true);
        hibernateUtils.saveOrUpdateEntity(admin);

        if (getFeature(CommonConstants.Features.EMAIL_SETTINGS) == null)
            hibernateUtils.saveOrUpdateEntity(new FeatureEntity(CommonConstants.Features.EMAIL_SETTINGS, dateUtils.getCurrentDate()));
        if (getFeature(CommonConstants.Features.MOBILE_SETTINGS) == null)
            hibernateUtils.saveOrUpdateEntity(new FeatureEntity(CommonConstants.Features.MOBILE_SETTINGS, dateUtils.getCurrentDate()));
        if (getFeature(CommonConstants.Features.AI_SETTINGS) == null)
            hibernateUtils.saveOrUpdateEntity(new FeatureEntity(CommonConstants.Features.AI_SETTINGS, dateUtils.getCurrentDate()));
        if (getFeature(CommonConstants.Features.COUPON_SETTINGS) == null)
            hibernateUtils.saveOrUpdateEntity(new FeatureEntity(CommonConstants.Features.COUPON_SETTINGS, dateUtils.getCurrentDate()));
        if (getFeature(CommonConstants.Features.PAYSLIP_SETTINGS) == null)
            hibernateUtils.saveOrUpdateEntity(new FeatureEntity(CommonConstants.Features.PAYSLIP_SETTINGS, dateUtils.getCurrentDate()));
        if (getFeature(CommonConstants.Features.KEYSTORE_SETTINGS) == null)
            hibernateUtils.saveOrUpdateEntity(new FeatureEntity(CommonConstants.Features.KEYSTORE_SETTINGS, dateUtils.getCurrentDate()));
    }

    private FeatureEntity getFeature(String featureName) {
        try (Session session = hibernateUtils.getSession()) {
            return (FeatureEntity) session.createQuery("FROM FeatureEntity WHERE featureName=:featureName").setParameter("featureName", featureName).uniqueResult();
        }
    }

    private UserEntity getUserEntity(String userName) {
        try (Session session = hibernateUtils.getSession()) {
            return (UserEntity) session.createQuery("FROM UserEntity WHERE userName=:userName").setParameter("userName", userName).uniqueResult();
        }
    }

}

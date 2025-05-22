package com.worktree.hrms.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.dao.CommonDao;
import com.worktree.hrms.entity.CouponEntity;
import com.worktree.hrms.exceptions.ForbiddenException;
import com.worktree.hrms.exceptions.PaymentRequiredException;
import com.worktree.hrms.utils.DateUtils;
import com.worktree.hrms.utils.HibernateUtils;
import com.worktree.hrms.utils.RequestHelper;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class CommonDaoImpl implements CommonDao {

    private final HibernateUtils hibernateUtils;

    private final DateUtils dateUtils;

    private static final String LICENCE_FILE_EXPIRED = "Licence file expired";

    private static final String COUPON_CODE = "couponCode";

    @Override
    public void userHasFeature(String feature) {
        try (Session session = hibernateUtils.getSession()) {
            String featureName = session.createQuery("SELECT fe.featureName FROM UserFeatures uf JOIN FeatureEntity fe on (uf.featureId=fe.featureId) WHERE uf.userID=:userID AND fe.featureName=:featureName", String.class)
                    .setParameter("userID", getLoggedInUserId()).setParameter("featureName", feature).uniqueResult();
            if (featureName == null) {
                throw new ForbiddenException(CommonConstants.ACCESS_DENIED);
            }
        }
    }

    @Override
    public Long getLoggedInUserId() {
        try (Session session = hibernateUtils.getSession()) {
            return session.createQuery("SELECT userID FROM UserTokenEntity WHERE jwt=:jwt", Long.class)
                    .setParameter("jwt", RequestHelper.getAuthorizationToken()).uniqueResult();
        }
    }

    @Override
    public Map<String, Object> getCoupons() {

        Map<String, Object> couponResponse = new HashMap<>();
        List<CouponEntity> couponEntities;
        try (Session session = hibernateUtils.getSession()) {
            couponEntities = session.createQuery("FROM CouponEntity", CouponEntity.class).list();
        }

        if (!CollectionUtils.isEmpty(couponEntities)) {
            List<Map<String, Object>> coupons = new ArrayList<>();
            int i = 1;
            for (CouponEntity couponEntity : couponEntities) {
                Map<String, Object> coupon = new HashMap<>();
                coupon.put("sNo", i);
                coupon.put(COUPON_CODE, couponEntity.getCouponCode());
                coupon.put("discountPercentage", couponEntity.getDiscountPercentage());
                coupon.put("createdDate", couponEntity.getCouponCreatedDate());
                coupon.put("createdBy", couponEntity.getUserID());
                coupon.put("status", couponEntity.getCouponStatus());
                i++;
                coupons.add(coupon);
            }
            couponResponse.put("coupons", coupons);
        } else {
            couponResponse.put("coupons", Arrays.asList());
        }
        return couponResponse;
    }

    @Override
    public Map<String, Object> saveCoupon(Map<String, Object> payload) {

        CouponEntity couponEntity = hibernateUtils.findEntityByCriteria(CouponEntity.class,
                COUPON_CODE, payload.get(COUPON_CODE).toString());

        if (couponEntity == null) {
            couponEntity = new CouponEntity();
            couponEntity.setCouponCreatedDate(dateUtils.getCurrentDate());
        }

        couponEntity.setCouponCode(payload.get(COUPON_CODE).toString());
        couponEntity.setDiscountPercentage(payload.get("discountPercentage").toString());

        couponEntity.setCouponStatus(Boolean.valueOf(payload.get("status").toString()));

        couponEntity.setUserID(getLoggedInUserId());

        hibernateUtils.saveOrUpdateEntity(couponEntity);

        return CommonConstants.SUCCESS_RESPONSE;
    }

    @Override
    public void validLicense() {
        try (Session session = hibernateUtils.getSession()) {
            List<String> licences = session.createQuery("SELECT licence FROM Licence", String.class).list();
            if (!CollectionUtils.isEmpty(licences)) {
                Map<String, Object> response = new ObjectMapper().readValue(licences.get(0), Map.class);

                if (DateUtils.isDateExceeded(response.get("licenseValidTill").toString(), DateUtils.LICENSE_DATE_FORMAT)) {
                    throw new PaymentRequiredException(LICENCE_FILE_EXPIRED);
                }
            } else {
                throw new PaymentRequiredException(LICENCE_FILE_EXPIRED);
            }
        } catch (Exception e) {
            throw new PaymentRequiredException(LICENCE_FILE_EXPIRED);
        }
    }

}

package com.worktree.hrms.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "t_coupons", schema = "admin_hrms", catalog = "admin_hrms")
public class CouponEntity {

    @Id
    @Column(name = "coupon_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = ".seq_coupon_id")
    private Long couponId;

    @Column(name = "coupon_code", unique = true, nullable = false)
    private String couponCode;

    @Column(name = "discount_percentage", nullable = false)
    private String discountPercentage;

    @Column(name = "coupon_status", nullable = false)
    private Boolean couponStatus;

    @Column(name = "coupon_created_date")
    private Date couponCreatedDate;

    @Column(name = "user_id", nullable = false)
    private Long userID;

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Boolean getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(Boolean couponStatus) {
        this.couponStatus = couponStatus;
    }

    public Date getCouponCreatedDate() {
        return couponCreatedDate;
    }

    public void setCouponCreatedDate(Date couponCreatedDate) {
        this.couponCreatedDate = couponCreatedDate;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}

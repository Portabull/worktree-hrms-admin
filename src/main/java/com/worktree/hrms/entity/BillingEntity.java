package com.worktree.hrms.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "t_billing", schema = "admin_hrms", catalog = "admin_hrms")
public class BillingEntity {

    @Id
    @Column(name = "billing_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = ".seq_billing_id")
    private Long billingId;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "bill_amount")
    private Double billAmount;

    @Column(name = "users_count")
    private Integer usersCount;

    @Column(name = "coupon_code")
    private String couponCode;

    @Column(name = "licence_type")
    private String licenceType;

    @Column(name = "licence_expiry_date")
    private Date licenceExpiryDate;

    //billed date once we save new insert
    @Column(name = "billed_date")
    private Date billedDate;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "is_billing_active")
    private Boolean isBillingActive;

    //base64 image/zip
    @Column(name = "payment_screenshot", columnDefinition = "TEXT")
    private String paymentScreenshot;

    public Long getBillingId() {
        return billingId;
    }

    public void setBillingId(Long billingId) {
        this.billingId = billingId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Double billAmount) {
        this.billAmount = billAmount;
    }

    public Integer getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getLicenceType() {
        return licenceType;
    }

    public void setLicenceType(String licenceType) {
        this.licenceType = licenceType;
    }

    public Date getLicenceExpiryDate() {
        return licenceExpiryDate;
    }

    public void setLicenceExpiryDate(Date licenceExpiryDate) {
        this.licenceExpiryDate = licenceExpiryDate;
    }

    public Date getBilledDate() {
        return billedDate;
    }

    public void setBilledDate(Date billedDate) {
        this.billedDate = billedDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Boolean getBillingActive() {
        return isBillingActive;
    }

    public void setBillingActive(Boolean billingActive) {
        isBillingActive = billingActive;
    }

    public String getPaymentScreenshot() {
        return paymentScreenshot;
    }

    public void setPaymentScreenshot(String paymentScreenshot) {
        this.paymentScreenshot = paymentScreenshot;
    }
}

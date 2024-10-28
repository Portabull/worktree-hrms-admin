package com.worktree.hrms.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "t_tenants", schema = "admin_hrms", catalog = "admin_hrms")
public class TenantEntity {

    @Id
    @Column(name = "tenant_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = ".seq_tenant_id")
    private Long tenantId;

    @Column(name = "tenant_name")
    private String tenantName;

    @Column(name = "personal_email")
    private String personalEmail;

    @Column(name = "company_email")
    private String companyEmail;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_pan")
    private String companyPan;

    @Column(name = "company_tin")
    private String companyTin;

    @Column(name = "company_address")
    private String companyAddress;

    @Column(name = "company_website")
    private String companyWebsite;

    //how many users per tenant
    @Column(name = "tenet_users")
    private Integer tenantUsers;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "tenant_created_date")
    private Date tenantCreatedDate;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyPan() {
        return companyPan;
    }

    public void setCompanyPan(String companyPan) {
        this.companyPan = companyPan;
    }

    public String getCompanyTin() {
        return companyTin;
    }

    public void setCompanyTin(String companyTin) {
        this.companyTin = companyTin;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Integer getTenantUsers() {
        return tenantUsers;
    }

    public void setTenantUsers(Integer tenantUsers) {
        this.tenantUsers = tenantUsers;
    }

    public Date getTenantCreatedDate() {
        return tenantCreatedDate;
    }

    public void setTenantCreatedDate(Date tenantCreatedDate) {
        this.tenantCreatedDate = tenantCreatedDate;
    }
}

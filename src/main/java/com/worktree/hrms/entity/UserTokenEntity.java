package com.worktree.hrms.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "t_user_token", schema = "admin_hrms", catalog = "admin_hrms")
public class UserTokenEntity {

    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = ".seq_user_admin_token_id")
    private Long tokenId;

    @Column(name = "user_id")
    private Long userID;

    @Column(name = "jwt", unique = true)
    private String jwt;

    @Column(name = "location")
    private String location;

    @Column(name = "logged_at")
    private Date date;

    @Column(name = "device_details", length = 700)
    private String deviceDetails;

    @Column(name = "license_verified")
    private Boolean licenseVerified;

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDeviceDetails() {
        return deviceDetails;
    }

    public void setDeviceDetails(String deviceDetails) {
        this.deviceDetails = deviceDetails;
    }

    public Boolean getLicenseVerified() {
        return licenseVerified;
    }

    public void setLicenseVerified(Boolean licenseVerified) {
        this.licenseVerified = licenseVerified;
    }
}

package com.worktree.hrms.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "t_user_features", schema = "admin_hrms", catalog = "admin_hrms")
public class UserFeatures {

    @Id
    @Column(name = "user_feature_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = ".seq_user_feature_admin_id")
    private Long userFeatureId;

    @Column(name = "feature_id")
    private Long featureId;

    @Column(name = "user_id")
    private Long userID;

    public Long getUserFeatureId() {
        return userFeatureId;
    }

    public void setUserFeatureId(Long userFeatureId) {
        this.userFeatureId = userFeatureId;
    }

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}

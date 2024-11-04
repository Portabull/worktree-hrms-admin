package com.worktree.hrms.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "t_features", schema = "admin_hrms", catalog = "admin_hrms")
public class FeatureEntity {

    @Id
    @Column(name = "feature_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = ".seq_feature_admin_id")
    private Long featureId;

    @Column(name = "feature_name", unique = true)
    private String featureName;

    @Column(name = "created_date")
    private Date createdDate;

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public FeatureEntity() {
    }

    public FeatureEntity(String featureName, Date createdDate) {
        this.featureName = featureName;
        this.createdDate = createdDate;
    }
}

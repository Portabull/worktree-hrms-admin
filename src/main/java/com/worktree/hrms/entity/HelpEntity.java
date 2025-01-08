package com.worktree.hrms.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "t_help_config", schema = "admin_hrms", catalog = "admin_hrms")
public class HelpEntity {

    @Id
    @Column(name = "help_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = ".seq_help_id")
    private Long helpId;

    @Column(name = "help_config_name", unique = true)
    private String helpConfigName;

    @Column(name = "help_config", columnDefinition = "TEXT")
    private String helpConfiguration;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    public Long getHelpId() {
        return helpId;
    }

    public void setHelpId(Long helpId) {
        this.helpId = helpId;
    }

    public String getHelpConfigName() {
        return helpConfigName;
    }

    public void setHelpConfigName(String helpConfigName) {
        this.helpConfigName = helpConfigName;
    }

    public String getHelpConfiguration() {
        return helpConfiguration;
    }

    public void setHelpConfiguration(String helpConfiguration) {
        this.helpConfiguration = helpConfiguration;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}

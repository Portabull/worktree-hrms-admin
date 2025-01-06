package com.worktree.hrms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "licence", schema = "admin_hrms", catalog = "admin_hrms")
public class Licence {
    @Id
    @Column(name = "licence", unique = true)
    private String licence;

    public Licence(String licence) {
        this.licence = licence;
    }

    public String getLicence() {
        return licence;
    }
}

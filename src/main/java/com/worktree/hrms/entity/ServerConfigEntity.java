package com.worktree.hrms.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "t_server_config", schema = "admin_hrms", catalog = "admin_hrms")
public class ServerConfigEntity {

    @Id
    @Column(name = "server_config_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = ".seq_server_cfg_admin_id")
    private Long serverConfigId;

    @Column(name = "server_config_name", unique = true)
    private String serverConfigName;

    @Column(name = "server_config", columnDefinition = "TEXT")
    private String serverConfiguration;

    public Long getServerConfigId() {
        return serverConfigId;
    }

    public void setServerConfigId(Long serverConfigId) {
        this.serverConfigId = serverConfigId;
    }

    public String getServerConfigName() {
        return serverConfigName;
    }

    public void setServerConfigName(String serverConfigName) {
        this.serverConfigName = serverConfigName;
    }

    public String getServerConfiguration() {
        return serverConfiguration;
    }

    public void setServerConfiguration(String serverConfiguration) {
        this.serverConfiguration = serverConfiguration;
    }
}

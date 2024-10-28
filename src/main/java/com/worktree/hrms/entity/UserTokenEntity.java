package com.worktree.hrms.entity;

import jakarta.persistence.*;

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
}

package com.pip.lab4.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserAccount {
    @Id
    private Long id;

    private Integer passwordHash;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(Integer passwordHash) {
        this.passwordHash = passwordHash;
    }
}

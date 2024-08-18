package com.meusboleto.backend.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class User {
    @Id
    @Column(name="id")
    private int id;
    
    @Column(name="user_name")
    private String userName;

    @Column(name="created_at")
    private Date createdAt;

    @Column(name="changed_at")
    private Date changedAt;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Date changedAt) {
        this.changedAt = changedAt;
    }


}

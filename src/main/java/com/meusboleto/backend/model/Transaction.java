package com.meusboleto.backend.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class Transaction implements Serializable {

    @Id
    @Column(name="id")
    private int id;

    @Column(name="transaction_name")
    private String transactionName;

    @Column(name="transaction_type")
    private String transactionType;

    private User user;
    
    public int getId() {
        return id;
    }

    public String getTransactionName() {
        return transactionName;
    }
    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }
    public String getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}

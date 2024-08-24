package com.meusboleto.backend.DTO;

import java.util.Date;


public class TransactionDTO {

    private int id;
    private String transactionName;
    private CategoryDTO category;
    private String description;
    private UserDTO user;
    private Date createdAt;
    private Date changedAt;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTransactionName() {
        return transactionName;
    }
    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }
    public CategoryDTO getCategory() {
        return category;
    }
    public void setCategory(CategoryDTO category) {
        this.category = category;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public UserDTO getUser() {
        return user;
    }
    public void setUser(UserDTO user) {
        this.user = user;
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

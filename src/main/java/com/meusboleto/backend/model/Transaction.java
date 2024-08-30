package com.meusboleto.backend.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
@Entity
@Table(name="transaction", uniqueConstraints= {
    @UniqueConstraint(columnNames={"category_id", "month_data_id", "user_id"})
})
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="transaction_name")
    private String transactionName;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    //@JsonBackReference
    private Category category;
    
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    //@JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "month_data_id", referencedColumnName = "id")
    //@JsonBackReference
    private MonthlyData monthlyData;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "changed_at")
    private Date changedAt;

    @Column(name = "transaction_value", precision = 19, scale = 2)
    private BigDecimal transactionValue;

    @Column(name = "transaction_budget", precision = 19, scale = 2)
    private BigDecimal transactionBudget;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(BigDecimal transactionValue) {
        this.transactionValue = transactionValue;
    }

    public BigDecimal getTransactionBudget() {
        return transactionBudget;
    }

    public void setTransactionBudget(BigDecimal transactionBudget) {
        this.transactionBudget = transactionBudget;
    }

    public MonthlyData getMonthlyData(){
        return monthlyData;
    }

    public void setMonthlyData(MonthlyData monthlyData) {
        this.monthlyData = monthlyData;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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


    public int getId() {
        return id;
    }

    public String getTransactionName() {
        return transactionName;
    }
    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }
    public Category getTransactionType() {
        return category;
    }
    public void setTransactionType(Category transactionType) {
        this.category = transactionType;
    }

}

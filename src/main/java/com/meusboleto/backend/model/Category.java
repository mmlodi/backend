package com.meusboleto.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="category_name")
    private String categoryName;

    
    @Enumerated(EnumType.STRING)  // Use EnumType.STRING to store the name of the enum constant in the database
    @Column(name = "category_type")
    private CategoryType tipoCategoria;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    //@JsonBackReference
    private User user;

    public Category(int id) {
        this.id = id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public CategoryType getTipoCategoria() {
        return tipoCategoria;
    }

    public void setTipoCategoria(CategoryType tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

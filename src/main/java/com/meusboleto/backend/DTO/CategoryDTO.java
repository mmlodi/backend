package com.meusboleto.backend.DTO;

import com.meusboleto.backend.model.CategoryType;



public class CategoryDTO {

    private int id;
    private String categoryName;
    private CategoryType tipoCategoria;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public CategoryType getTipoCategoria() {
        return tipoCategoria;
    }
    public void setTipoCategoria(CategoryType tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }
}

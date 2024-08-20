package com.meusboleto.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meusboleto.backend.model.Category;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

}

package com.meusboleto.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meusboleto.backend.model.Category;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    List<Category> findByUserId(int userId);
}

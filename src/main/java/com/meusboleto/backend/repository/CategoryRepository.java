package com.meusboleto.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meusboleto.backend.model.Category;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    List<Category> findByUserId(int userId);
    Optional<Category> findByIdAndUserId(int id, int userId);
}

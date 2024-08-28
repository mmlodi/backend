package com.meusboleto.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meusboleto.backend.model.MonthlyData;

public interface MonthlyDataRepository extends JpaRepository<MonthlyData, Integer> {
    Optional<MonthlyData> findByMonthAndYear(Integer month, Integer year);
}

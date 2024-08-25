package com.meusboleto.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meusboleto.backend.model.MonthlyData;

public interface MonthlyDataRepository extends JpaRepository<MonthlyData, Integer> {

}

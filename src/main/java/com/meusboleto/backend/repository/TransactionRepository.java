package com.meusboleto.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meusboleto.backend.model.Transaction;

public interface  TransactionRepository extends JpaRepository<Transaction, Integer> {

}

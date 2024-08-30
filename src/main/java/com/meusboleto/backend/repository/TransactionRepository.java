package com.meusboleto.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.meusboleto.backend.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    // @Query("SELECT c, m, t FROM MonthlyData m " +
    //         "CROSS JOIN Category c " +
    //         "LEFT JOIN Transaction t ON c.id = t.category.id AND t.monthDateId.id = m.id AND t.user.id = :userId " +
    //         "WHERE t.user.id = :userId OR t.user.id IS NULL " +
    //         "ORDER BY m.year, m.month, c.categoryName")
    @Query("SELECT t FROM Transaction t " +
           "JOIN FETCH t.category c " +
           "JOIN FETCH t.monthlyData m " +
           "WHERE t.user.id = :userId " +
           "ORDER BY m.year, m.month, c.categoryName")
    List<Transaction> findAllCategoriesWithTransactionsForUser(@Param("userId") int userId);

    Optional<Transaction> findByCategoryIdAndMonthlyDataIdAndUserId(Integer categoryId, Integer monthlyDataId, Integer userId);
}


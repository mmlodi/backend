package com.meusboleto.backend.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meusboleto.backend.DTO.TransactionDTO;
import com.meusboleto.backend.model.MonthlyData;
import com.meusboleto.backend.model.Transaction;
import com.meusboleto.backend.repository.MonthlyDataRepository;
import com.meusboleto.backend.repository.TransactionRepository;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MonthlyDataRepository monthlyDataRepository;

    @Autowired
    private ModelMapper mapper;

    // @GetMapping
    // public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
    //     List<Transaction> transactions = transactionRepository.findAll();
    //     List<TransactionDTO> transactionDTOs = transactions.stream()
    //             .map(e -> mapper.map(e, TransactionDTO.class))
    //             .collect(Collectors.toList());
    //     return ResponseEntity.ok(transactionDTOs);
    // }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable int id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            TransactionDTO transactionDTO = mapper.map(transaction.get(), TransactionDTO.class);
            return ResponseEntity.ok(transactionDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionDTO>> getCategoriesWithTransactions(@PathVariable int userId) {
        List<TransactionDTO> results = getAllDataFromUser(userId);
        return ResponseEntity.ok(results);
    }

    public List<TransactionDTO> getAllDataFromUser(int userId) {
        List<Transaction> transactions = transactionRepository.findAllCategoriesWithTransactionsForUser(userId);

        return transactions.stream()
                           .map(transaction -> mapper.map(transaction, TransactionDTO.class))
                           .collect(Collectors.toList());
        // List<Object[]> results = transactionRepository.findAllCategoriesWithTransactionsForUser(userId);
        // List<TransactionDTO> transactionDTOs = new ArrayList<>();

        // for (Object[] result : results) {
        //     //Category category = (Category) result[0];
        //     MonthlyData monthlyData = (MonthlyData) result[1];
        //     Transaction transaction = (Transaction) result[2];

        //     TransactionDTO transactionDTO = new TransactionDTO();
        //    // transactionDTO.setCategory(modelMapper.map(category, CategoryDTO.class));
        //     transactionDTO.setMonthlyData(monthlyData);

        //     if (transaction != null) {
        //         transactionDTO.setId(transaction.getId());
        //         transactionDTO.setTransactionName(transaction.getTransactionName());
        //         transactionDTO.setDescription(transaction.getDescription());
        //         transactionDTO.setUser(mapper.map(transaction.getUser(), UserDTO.class));
        //         transactionDTO.setCreatedAt(transaction.getCreatedAt());
        //         transactionDTO.setChangedAt(transaction.getChangedAt());
        //         transactionDTO.setTransactionValue(transaction.getTransactionValue());
        //         transactionDTO.setTransactionBudget(transaction.getTransactionBudget());
        //     } else {
        //         transactionDTO.setTransactionName(null);
        //         transactionDTO.setDescription(null);
        //         transactionDTO.setUser(null);
        //         transactionDTO.setCreatedAt(null);
        //         transactionDTO.setChangedAt(null);
        //         transactionDTO.setTransactionValue(null);
        //         transactionDTO.setTransactionBudget(null);
        //     }

        //     transactionDTOs.add(transactionDTO);
        // }

        // return transactionDTOs;
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody Transaction transaction) {
        // Ensure the MonthlyData entity exists
        Optional<MonthlyData> monthData = monthlyDataRepository.findById(transaction.getMonthlyData().getId());
        if (!monthData.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        transaction.setMonthlyData(monthData.get());

        // Save the transaction
        Transaction savedTransaction = transactionRepository.save(transaction);
        TransactionDTO transactionDTO = mapper.map(savedTransaction, TransactionDTO.class);

        return ResponseEntity.ok(transactionDTO);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable int id, @RequestBody Transaction transactionDetails) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            Transaction updatedTransaction = transaction.get();
            updatedTransaction.setTransactionName(transactionDetails.getTransactionName());
            updatedTransaction.setTransactionType(transactionDetails.getTransactionType());
            updatedTransaction.setDescription(transactionDetails.getDescription());
            updatedTransaction.setUser(transactionDetails.getUser());

            // Ensure the MonthlyData entity exists
            Optional<MonthlyData> monthData = monthlyDataRepository.findById(transactionDetails.getMonthlyData().getId());
            if (!monthData.isPresent()) {
                return ResponseEntity.badRequest().build();
            }
            updatedTransaction.setMonthlyData(monthData.get());

            updatedTransaction.setCreatedAt(transactionDetails.getCreatedAt());
            updatedTransaction.setChangedAt(transactionDetails.getChangedAt());

            transactionRepository.save(updatedTransaction);
            return ResponseEntity.ok(updatedTransaction);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable int id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            transactionRepository.delete(transaction.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

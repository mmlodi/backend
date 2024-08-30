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
import com.meusboleto.backend.repository.CategoryRepository;
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
    private CategoryRepository categoryRepository;

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
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody Transaction transaction) {
        Integer month = transaction.getMonthlyData().getMonth();
        Integer year = transaction.getMonthlyData().getYear();
        Integer categoryId = transaction.getCategory().getId();
    
        Optional<MonthlyData> monthlyData = monthlyDataRepository.findByMonthAndYear(month, year);
        if (!monthlyData.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        MonthlyData monthlyDataId = monthlyData.get();
        
        Optional<Transaction> existingTransactionOpt = transactionRepository.findByCategoryIdAndMonthlyDataIdAndUserId(categoryId, monthlyDataId.getId(), transaction.getUser().getId());

    
        transaction.setMonthlyData(monthlyData.get());

        Transaction trans;
        if (existingTransactionOpt.isPresent()) {
            Transaction updatedTransaction = existingTransactionOpt.get();
    
            //updatedTransaction.setTransactionName(transaction.getTransactionName());
            //updatedTransaction.setTransactionType(transaction.getTransactionType());
            //updatedTransaction.setDescription(transaction.getDescription());
            //updatedTransaction.setUser(transaction.getUser());
            //updatedTransaction.setMonthlyData(monthlyData.get());
            //updatedTransaction.setCreatedAt(transaction.getCreatedAt());
            updatedTransaction.setTransactionValue(transaction.getTransactionValue());
            updatedTransaction.setTransactionBudget(transaction.getTransactionBudget());
            updatedTransaction.setChangedAt(transaction.getChangedAt());
            //updatedTransaction.setCategory(transaction.getCategory());
    
            trans = transactionRepository.save(updatedTransaction);
        } else {
            trans = transactionRepository.save(transaction);
        }
    
        TransactionDTO transactionDTO = mapper.map(trans, TransactionDTO.class);
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

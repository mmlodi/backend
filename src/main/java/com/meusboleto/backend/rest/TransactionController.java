package com.meusboleto.backend.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meusboleto.backend.DTO.TransactionDTO;
import com.meusboleto.backend.model.Category;
import com.meusboleto.backend.model.MonthlyData;
import com.meusboleto.backend.model.Transaction;
import com.meusboleto.backend.model.User;
import com.meusboleto.backend.repository.CategoryRepository;
import com.meusboleto.backend.repository.MonthlyDataRepository;
import com.meusboleto.backend.repository.TransactionRepository;
import com.meusboleto.backend.repository.UserRepository;
import com.meusboleto.backend.service.UserDetailsImpl;

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
    private UserRepository userRepository;

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
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable int id, Authentication authentication) {
        Optional<Transaction> transaction = transactionRepository.findByIdAndUserId(id, currentUserId(authentication));
        if (transaction.isPresent()) {
            TransactionDTO transactionDTO = mapper.map(transaction.get(), TransactionDTO.class);
            return ResponseEntity.ok(transactionDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<List<TransactionDTO>> getCurrentUserTransactions(Authentication authentication) {
        List<TransactionDTO> results = getAllDataFromUser(currentUserId(authentication));
        return ResponseEntity.ok(results);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionDTO>> getCategoriesWithTransactions(@PathVariable int userId, Authentication authentication) {
        if (userId != currentUserId(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

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
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody Transaction transaction, Authentication authentication) {
        int currentUserId = currentUserId(authentication);
        Integer month = transaction.getMonthlyData().getMonth();
        Integer year = transaction.getMonthlyData().getYear();
        Integer categoryId = transaction.getCategory().getId();
    
        Optional<MonthlyData> monthlyData = monthlyDataRepository.findByMonthAndYear(month, year);
        if (!monthlyData.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        MonthlyData monthlyDataId = monthlyData.get();

        Optional<Category> category = categoryRepository.findByIdAndUserId(categoryId, currentUserId);
        if (!category.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User currentUser = userRepository.findById(currentUserId).orElseThrow();
        
        Optional<Transaction> existingTransactionOpt = transactionRepository.findByCategoryIdAndMonthlyDataIdAndUserId(categoryId, monthlyDataId.getId(), currentUserId);

    
        transaction.setMonthlyData(monthlyData.get());
        transaction.setCategory(category.get());
        transaction.setUser(currentUser);

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
            //transaction.setId(null);
            trans = transactionRepository.save(transaction);
        }
    
        TransactionDTO transactionDTO = mapper.map(trans, TransactionDTO.class);
        return ResponseEntity.ok(transactionDTO);
    }



    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable int id, @RequestBody Transaction transactionDetails, Authentication authentication) {
        int currentUserId = currentUserId(authentication);
        Optional<Transaction> transaction = transactionRepository.findByIdAndUserId(id, currentUserId);
        if (transaction.isPresent()) {
            Transaction updatedTransaction = transaction.get();
            updatedTransaction.setTransactionName(transactionDetails.getTransactionName());
            updatedTransaction.setDescription(transactionDetails.getDescription());

            if (transactionDetails.getCategory() != null) {
                Optional<Category> category = categoryRepository.findByIdAndUserId(transactionDetails.getCategory().getId(), currentUserId);
                if (!category.isPresent()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
                updatedTransaction.setCategory(category.get());
            }

            // Ensure the MonthlyData entity exists
            Optional<MonthlyData> monthData = monthlyDataRepository.findById(transactionDetails.getMonthlyData().getId());
            if (!monthData.isPresent()) {
                return ResponseEntity.badRequest().build();
            }
            updatedTransaction.setMonthlyData(monthData.get());

            updatedTransaction.setCreatedAt(transactionDetails.getCreatedAt());
            updatedTransaction.setChangedAt(transactionDetails.getChangedAt());

            transactionRepository.save(updatedTransaction);
            return ResponseEntity.ok(mapper.map(updatedTransaction, TransactionDTO.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable int id, Authentication authentication) {
        Optional<Transaction> transaction = transactionRepository.findByIdAndUserId(id, currentUserId(authentication));
        if (transaction.isPresent()) {
            transactionRepository.delete(transaction.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private int currentUserId(Authentication authentication) {
        return ((UserDetailsImpl) authentication.getPrincipal()).getId();
    }
}

package com.meusboleto.backend;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meusboleto.backend.model.Category;
import com.meusboleto.backend.model.CategoryType;
import com.meusboleto.backend.model.MonthlyData;
import com.meusboleto.backend.model.Transaction;
import com.meusboleto.backend.model.User;
import com.meusboleto.backend.repository.CategoryRepository;
import com.meusboleto.backend.repository.MonthlyDataRepository;
import com.meusboleto.backend.repository.TransactionRepository;
import com.meusboleto.backend.repository.UserRepository;
import com.meusboleto.backend.security.jwt.JwtUtils;
import com.meusboleto.backend.service.UserDetailsImpl;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityAccessTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MonthlyDataRepository monthlyDataRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private User owner;
    private User otherUser;
    private Category otherCategory;
    private MonthlyData month;
    private String ownerToken;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        categoryRepository.deleteAll();
        monthlyDataRepository.deleteAll();
        userRepository.deleteAll();

        owner = userRepository.save(user("owner", "owner@example.com"));
        otherUser = userRepository.save(user("other", "other@example.com"));

        otherCategory = categoryRepository.save(category("Other bills", otherUser));
        month = monthlyDataRepository.save(monthlyData(0, 2026));

        ownerToken = jwtUtils.generateTokenFromUserDetailsImplementation(UserDetailsImpl.build(owner));
    }

    @Test
    void getUsersRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void signupReturnsUserDtoWithoutPasswordHash() throws Exception {
        User newUser = user("new-user", "new-user@example.com");
        newUser.setSenha("plain-password");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.userName").value("new-user"))
            .andExpect(jsonPath("$", not(hasKey("senha"))))
            .andExpect(jsonPath("$", not(hasKey("password"))));
    }

    @Test
    void cannotReadAnotherUsersCategoriesByPathId() throws Exception {
        mockMvc.perform(get("/api/categories/user/{userId}", otherUser.getId())
                .header("Authorization", bearer(ownerToken)))
            .andExpect(status().isForbidden());
    }

    @Test
    void cannotReadAnotherUsersTransactionsByPathId() throws Exception {
        transactionRepository.save(transaction(otherCategory, otherUser, month));

        mockMvc.perform(get("/api/transactions/user/{userId}", otherUser.getId())
                .header("Authorization", bearer(ownerToken)))
            .andExpect(status().isForbidden());
    }

    @Test
    void cannotCreateTransactionForAnotherUsersCategory() throws Exception {
        Transaction request = transaction(otherCategory, otherUser, month);

        mockMvc.perform(post("/api/transactions")
                .header("Authorization", bearer(ownerToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    private User user(String username, String email) {
        User user = new User();
        user.setUserName(username);
        user.setEmail(email);
        user.setSenha(passwordEncoder.encode("password"));
        user.setCreatedAt(new Date());
        return user;
    }

    private Category category(String name, User user) {
        Category category = new Category();
        category.setCategoryName(name);
        category.setTipoCategoria(CategoryType.BOLETO);
        category.setUser(user);
        return category;
    }

    private MonthlyData monthlyData(int month, int year) {
        MonthlyData monthlyData = new MonthlyData();
        monthlyData.setMonth(month);
        monthlyData.setYear(year);
        return monthlyData;
    }

    private Transaction transaction(Category category, User user, MonthlyData monthlyData) {
        Transaction transaction = new Transaction();
        transaction.setCategory(category);
        transaction.setUser(user);
        transaction.setMonthlyData(monthlyData);
        transaction.setTransactionName(category.getCategoryName());
        transaction.setTransactionValue(BigDecimal.TEN);
        transaction.setTransactionBudget(BigDecimal.valueOf(100));
        return transaction;
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }
}

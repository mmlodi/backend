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

import com.meusboleto.backend.DTO.CategoryDTO;
import com.meusboleto.backend.model.Category;
import com.meusboleto.backend.model.User;
import com.meusboleto.backend.repository.CategoryRepository;
import com.meusboleto.backend.repository.UserRepository;
import com.meusboleto.backend.service.UserDetailsImpl;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories(Authentication authentication) {
        List<Category> categories = categoryRepository.findByUserId(currentUserId(authentication));

        List<CategoryDTO> categoriesDTO = categories.stream().map( e -> mapper.map(e, CategoryDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok(categoriesDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable int id, Authentication authentication) {
        Optional<Category> categoryOptional = categoryRepository.findByIdAndUserId(id, currentUserId(authentication));
        if (categoryOptional.isPresent()) {
            CategoryDTO categoryDTO = mapper.map(categoryOptional.get(), CategoryDTO.class);
            return ResponseEntity.ok(categoryDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<List<CategoryDTO>> getCurrentUserCategories(Authentication authentication) {
        return getAllCategories(authentication);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CategoryDTO>> getCategoryByUserId(@PathVariable int userId, Authentication authentication) {
        if (userId != currentUserId(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<CategoryDTO> category = getAllCategoriesFromUser(userId);
        return ResponseEntity.ok(category);
    }

    public List<CategoryDTO> getAllCategoriesFromUser(int userId) {
        List<Category> categories = categoryRepository.findByUserId(userId);

        return categories.stream()
            .map(category -> mapper.map(category, CategoryDTO.class))
            .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody Category category, Authentication authentication) {
        User currentUser = userRepository.findById(currentUserId(authentication)).orElseThrow();
        category.setUser(currentUser);
        Category savedCategory = categoryRepository.save(category);
        CategoryDTO categoryDTO = mapper.map(savedCategory, CategoryDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable int id, @RequestBody Category categoryDetails, Authentication authentication) {
        Optional<Category> category = categoryRepository.findByIdAndUserId(id, currentUserId(authentication));
        if (category.isPresent()) {
            Category updatedCategory = category.get();
            updatedCategory.setCategoryName(categoryDetails.getCategoryName());
            updatedCategory.setTipoCategoria(categoryDetails.getTipoCategoria());
            categoryRepository.save(updatedCategory);
            return ResponseEntity.ok(mapper.map(updatedCategory, CategoryDTO.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id, Authentication authentication) {
        Optional<Category> category = categoryRepository.findByIdAndUserId(id, currentUserId(authentication));
        if (category.isPresent()) {
            categoryRepository.delete(category.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private int currentUserId(Authentication authentication) {
        return ((UserDetailsImpl) authentication.getPrincipal()).getId();
    }
}

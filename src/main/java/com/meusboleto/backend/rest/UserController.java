package com.meusboleto.backend.rest;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meusboleto.backend.DTO.UserDTO;
import com.meusboleto.backend.model.User;
import com.meusboleto.backend.repository.UserRepository;
import com.meusboleto.backend.service.UserDetailsImpl;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        return userRepository.findById(currentUserId(authentication))
                .map(user -> ResponseEntity.ok(mapper.map(user, UserDTO.class)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id, Authentication authentication) {
        if (id != currentUserId(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return getCurrentUser(authentication);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        User u = mapper.map(user, User.class);

        u.setSenha(passwordEncoder.encode(u.getSenha()));
        User savedUser = userRepository.save(u);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(savedUser, UserDTO.class)) ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable int id, @RequestBody User userDetails, Authentication authentication) {
        if (id != currentUserId(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User updatedUser = user.get();
            if (userDetails.getSenha() != null && !userDetails.getSenha().isBlank()) {
                updatedUser.setSenha(passwordEncoder.encode(userDetails.getSenha()));
            }
            updatedUser.setEmail(userDetails.getEmail());
            updatedUser.setChangedAt(userDetails.getChangedAt());
            userRepository.save(updatedUser);
            return ResponseEntity.ok(mapper.map(updatedUser, UserDTO.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id, Authentication authentication) {
        if (id != currentUserId(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private int currentUserId(Authentication authentication) {
        return ((UserDetailsImpl) authentication.getPrincipal()).getId();
    }
}

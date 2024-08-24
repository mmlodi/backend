package com.meusboleto.backend.rest;

import com.meusboleto.backend.DTO.UserDTO;
import com.meusboleto.backend.model.User;
import com.meusboleto.backend.repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserDTO> userList = users.stream().map(e -> mapper.map(e, UserDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(e -> {
            UserDTO userDTO = mapper.map(e, UserDTO.class);
            return ResponseEntity.ok(userDTO);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User u = mapper.map(user, User.class);
        User usu = mapper.map(userRepository.save(u), User.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(usu) ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User userDetails) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setUserName(userDetails.getUserName());
            updatedUser.setSenha(userDetails.getSenha());
            updatedUser.setEmail(userDetails.getEmail());
            updatedUser.setCreatedAt(userDetails.getCreatedAt());
            updatedUser.setChangedAt(userDetails.getChangedAt());
            userRepository.save(updatedUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

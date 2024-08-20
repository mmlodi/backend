package com.meusboleto.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meusboleto.backend.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}

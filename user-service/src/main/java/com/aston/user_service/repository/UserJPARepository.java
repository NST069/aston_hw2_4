package com.aston.user_service.repository;

import com.aston.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJPARepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);
}

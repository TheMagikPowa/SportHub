package com.generation.SportHub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.SportHub.entity.User;

public interface UserRepository extends JpaRepository <User, Long>{

boolean existsByEmailIgnoreCase (String email);

Optional<User> findByEmail(String email);

}

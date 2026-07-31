package com.generation.SportHub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.SportHub.entity.Person;

public interface PersonRepository extends JpaRepository <Person, Long> {

    boolean existsByUsernameIgnoreCase (String username);

    Optional<Person> findByUsername(String username);
    
}

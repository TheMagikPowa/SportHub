package com.generation.SportHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.SportHub.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    
}

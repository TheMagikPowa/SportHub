package com.generation.SportHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.SportHub.entity.CartHasProducts;

public interface CartHasProductsRepository extends JpaRepository<CartHasProducts, Long> {
    
}

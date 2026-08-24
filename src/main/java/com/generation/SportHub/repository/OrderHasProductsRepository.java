package com.generation.SportHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.SportHub.entity.OrderHasProducts;

public interface OrderHasProductsRepository extends JpaRepository<OrderHasProducts, Long> {
    
}

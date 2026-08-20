package com.generation.SportHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.SportHub.entity.OrderHasProducts;

public interface OrderHasProductsRepositorys extends JpaRepository<OrderHasProducts, Long> {
    
}

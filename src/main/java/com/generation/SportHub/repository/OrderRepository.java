package com.generation.SportHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.SportHub.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}

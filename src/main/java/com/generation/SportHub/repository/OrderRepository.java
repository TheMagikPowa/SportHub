package com.generation.SportHub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.SportHub.entity.Buyer;
import com.generation.SportHub.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByBuyer(Buyer buyer);

    List<Order> findByBuyerId(Long buyerId);
}

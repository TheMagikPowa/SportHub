package com.generation.SportHub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.SportHub.entity.CartHasProducts;

public interface CartHasProductsRepository extends JpaRepository<CartHasProducts, Long> {
    
    List<CartHasProducts> findByCartId(Long id);

    Optional<CartHasProducts> findByCartIdAndProductId(Long cartId, Long productId);
}

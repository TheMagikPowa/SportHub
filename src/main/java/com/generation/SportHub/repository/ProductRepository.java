package com.generation.SportHub.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.SportHub.entity.Product;

public interface ProductRepository extends JpaRepository <Product, Long> {

}

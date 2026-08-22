package com.generation.SportHub.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.SportHub.entity.Product;
import com.generation.SportHub.entity.enums.AgeCategory;
import com.generation.SportHub.entity.enums.ProductGender;

public interface ProductRepository extends JpaRepository <Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByPGender(ProductGender pGender);

    List<Product> findByACategory(AgeCategory aCategory);
    
    
}


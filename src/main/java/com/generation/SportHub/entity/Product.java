package com.generation.SportHub.entity;

import java.math.BigDecimal;

import com.generation.SportHub.entity.enums.AgeCategory;
import com.generation.SportHub.entity.enums.ProductGender;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255) 
    private String name;

    @Column(nullable = false, precision = 10, scale = 2) 
    private BigDecimal price;
    
    @Column(nullable = false) 
    private Integer quantity;
    
    @Column(nullable = false, length = 2000) 
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private ProductGender pGender;  

    @Enumerated(EnumType.STRING)
    @Column(name = "age_category", nullable = false)
    private AgeCategory aCategory;  


}


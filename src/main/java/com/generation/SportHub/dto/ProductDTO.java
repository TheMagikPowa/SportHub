package com.generation.SportHub.dto;

import java.math.BigDecimal;

import com.generation.SportHub.entity.enums.AgeCategory;
import com.generation.SportHub.entity.enums.ProductCategory;
import com.generation.SportHub.entity.enums.ProductGender;

public record ProductDTO(
    Long id,
    String name,
    String description,
    Integer quantity,
    BigDecimal price,
    ProductGender pGender, 
    AgeCategory aCategory,
    ProductCategory category
) implements GenericDTO {

  
    
}

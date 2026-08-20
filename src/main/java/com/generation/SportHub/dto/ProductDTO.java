package com.generation.SportHub.dto;

public record ProductDTO(
    Long id,
    String name,
    String description,
    Integer quantity,
    Double price,
    String pGender,
    String aCategory
) implements GenericDTO {
    
}

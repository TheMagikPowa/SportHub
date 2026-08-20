package com.generation.SportHub.dto;

public record CartHasProductsDTO(
    Long id,
    Long cartId,
    Long productId,
    Integer quantity
) implements GenericDTO {
    
}

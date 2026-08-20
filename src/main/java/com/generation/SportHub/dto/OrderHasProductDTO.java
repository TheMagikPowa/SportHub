package com.generation.SportHub.dto;

public record OrderHasProductDTO(
    Long id,
    OrderDTO order,
    ProductDTO product,
    String name,
    Double unitPrice,
    Double finalPrice,
    Integer quantity
) implements GenericDTO {
    
}

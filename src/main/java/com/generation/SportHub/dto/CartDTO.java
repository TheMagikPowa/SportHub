package com.generation.SportHub.dto;

import java.time.Instant;

public record CartDTO(
    Long id,
    BuyerDTO buyer,
    Instant createdAt,
    Instant modifiedAt
) implements GenericDTO {
    
}

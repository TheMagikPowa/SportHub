package com.generation.SportHub.dto;

import java.time.Instant;

public record EventAnswerDTO(
    Long id,
    BuyerDTO buyer,
    String text,
    Instant createdAt,
    EventDTO event
) implements GenericDTO {
    
}

package com.generation.SportHub.dto;

public record BuyerSummaryDTO(
    Long id,
    String username,
    String name,
    String surname
) implements GenericDTO{
}

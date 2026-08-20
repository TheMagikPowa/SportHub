package com.generation.SportHub.dto;

import java.time.Instant;

/**
 * EventAnswerSummaryDTO
 */
public record EventAnswerSummaryDTO(
    Long id,
    BuyerSummaryDTO buyer,
    String text,
    Instant createdAt
) {

}

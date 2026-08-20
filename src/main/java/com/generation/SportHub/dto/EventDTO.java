package com.generation.SportHub.dto;

import java.time.Instant;
import java.time.LocalDate;

import java.util.List;

import com.generation.SportHub.entity.enums.EventType;

public record EventDTO (

    Long id,
    BuyerSummaryDTO buyer,
    String eventTitle,
    String message,
    LocalDate eventDate,
    Instant messageTime,
    EventType type,
    List<EventAnswerSummaryDTO> eventAnswers

) implements GenericDTO {
    
}

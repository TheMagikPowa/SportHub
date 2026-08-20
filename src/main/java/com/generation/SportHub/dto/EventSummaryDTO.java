package com.generation.SportHub.dto;

import java.time.LocalDate;

import com.generation.SportHub.entity.enums.EventType;

/**
 * EventSummaryDTO
 */
public record EventSummaryDTO(
    Long id,
    String eventTitle,
    LocalDate eventDate,
    EventType type) implements GenericDTO {

}

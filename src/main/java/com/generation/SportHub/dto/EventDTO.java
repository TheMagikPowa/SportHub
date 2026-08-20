package com.generation.SportHub.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import com.generation.SportHub.entity.Buyer;
import com.generation.SportHub.entity.enums.EventType;

public record EventDTO (

    Long id,
    Buyer buyer,
    String eventTitle,
    String message,
    LocalDateTime eventDate,
    Instant messageTime,
    EventType type,
    List<EventAnswerDTO> eventAnswers

) implements GenericDTO {
    
}

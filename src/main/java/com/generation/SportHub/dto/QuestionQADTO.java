package com.generation.SportHub.dto;

import java.time.Instant;

import com.generation.SportHub.entity.enums.QuestionStatus;

public record QuestionQADTO (
    Long id,
    BuyerDTO buyer,
    String title,
    String message,
    QuestionStatus status,
    Instant createTime
) implements GenericDTO {

}

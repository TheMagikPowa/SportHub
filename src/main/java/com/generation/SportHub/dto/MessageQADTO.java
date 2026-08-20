package com.generation.SportHub.dto;

import java.time.Instant;



public record MessageQADTO (
    Long id,
    QuestionQADTO question,
    PersonDTO staff,
    String text,
    Instant createdAt
) implements GenericDTO{


    
}

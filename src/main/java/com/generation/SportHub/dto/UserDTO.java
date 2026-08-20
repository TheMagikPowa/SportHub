package com.generation.SportHub.dto;

import java.time.Instant;

public record UserDTO(
    Long id,
    String email,
    Instant createTime
    
) implements GenericDTO {
} 

package com.generation.SportHub.dto;

import java.time.LocalDate;

import com.generation.SportHub.entity.enums.PersonGender;
import com.generation.SportHub.entity.enums.Role;

public record BuyerDTO(
    Long id,
    String email,
    String username,
    String name,
    String surname,
    LocalDate dob,
    PersonGender gender,
    Role role,
    boolean active
    
) implements GenericDTO {

}

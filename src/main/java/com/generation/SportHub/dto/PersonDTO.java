package com.generation.SportHub.dto;

import java.time.LocalDate;

import com.generation.SportHub.entity.enums.PersonGender;
import com.generation.SportHub.entity.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PersonDTO extends UserDTO {

    private String username;
    private String name;
    private String surname;
    private LocalDate dob;
    private PersonGender gender;
    private Role role;

  /* PersonDTO adminDto = PersonDTO.builder()
    .id(1L)
    .email("admin@email.com")
    .role(Role.ADMIN) 
    .build();

    PersonDTO staffDTO = PersonDTO.builder()
    .id(1L)
    .email("admin@email.com")
    .role(Role.STAFF) 
    .build(); 

    //risolto online, il dto vuoto genera errore con hibernate, suggerisce di ovviare così al problema  */
}

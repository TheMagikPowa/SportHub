package com.generation.SportHub.converters;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.generation.SportHub.dto.PersonDTO;
import com.generation.SportHub.entity.Person;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonConverter implements GenericConverter<PersonDTO, Person>{

    private final PasswordEncoder passwordEncoder;

    @Override
    public PersonDTO fromEntityToDto(Person entity) {
        return new PersonDTO(
            entity.getId(),
            entity.getEmail(),
            entity.getUsername(),
            entity.getName(),
            entity.getSurname(),
            entity.getDob() != null ? entity.getDob() : null,
            entity.getGender(),
            entity.getRole(), 
            entity.getPassword(), 
            null 
        ); 
    }

    @Override
    public Person fromDtoToEntity(PersonDTO dto) {
        Person entity = new Person();
        entity.setEmail(dto.email());
        entity.setUsername(dto.username());
        entity.setName(dto.name());
        entity.setSurname(dto.surname());
        entity.setDob(dto.dob() != null ? dto.dob() : null);
        entity.setGender(dto.gender());
        entity.setRole(dto.role());
        entity.setPassword(passwordEncoder.encode(dto.password()));
        return entity;
    }
}
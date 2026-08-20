package com.generation.SportHub.converters;

import com.generation.SportHub.dto.PersonDTO;
import com.generation.SportHub.entity.Person;

public class PersonConverter implements GenericConverter<PersonDTO, Person>{

    @Override
    public PersonDTO fromEntityToDto(Person entity) {
        return new PersonDTO(
        entity.getId(),
        entity.getEmail(),
        entity.getUsername(),
        entity.getName(),
        entity.getSurname(),
        entity.getDob() != null ? entity.getDob().toLocalDate() : null,
        entity.getGender(),
        entity.getRole()
        );
        
    }

    @Override
    public Person fromDtoToEntity(PersonDTO dto) {
        Person entity = new Person();
        entity.setUsername(dto.username());
        entity.setName(dto.name());
        entity.setSurname(dto.surname());
        entity.setDob(dto.dob() != null ? dto.dob().atStartOfDay() : null);
        entity.setGender(dto.gender());
        entity.setRole(dto.role());
        return entity;
    }
    
}

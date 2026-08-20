package com.generation.SportHub.converters;

import com.generation.SportHub.dto.BuyerDTO;
import com.generation.SportHub.entity.Buyer;


public class BuyerConverter implements GenericConverter<BuyerDTO, Buyer> {

    @Override
    public BuyerDTO fromEntityToDto(Buyer entity) {
        return new BuyerDTO(
            entity.getId(),
            entity.getEmail(),
            entity.getUsername(),
            entity.getName(),
            entity.getSurname(),
            entity.getDob() != null ? entity.getDob().toLocalDate() : null,
            entity.getGender(),
            entity.getRole(),
            entity.isActive()
        );
    }

    @Override
    public Buyer fromDtoToEntity(BuyerDTO dto) {
        Buyer entity = new Buyer();
        entity.setUsername(dto.username());
        entity.setName(dto.name());
        entity.setSurname(dto.surname());
        entity.setDob(dto.dob() != null ? dto.dob().atStartOfDay() : null);
        entity.setGender(dto.gender());
        entity.setRole(dto.role());
        return entity;
    }
    
}

package com.generation.SportHub.converters;

import org.springframework.stereotype.Service;

import com.generation.SportHub.dto.BuyerSummaryDTO;
import com.generation.SportHub.entity.Buyer;

@Service
public class BuyerSummaryConverter implements GenericConverter<BuyerSummaryDTO, Buyer> {

    @Override
    public BuyerSummaryDTO fromEntityToDto(Buyer entity) {
        return new BuyerSummaryDTO(
            entity.getId(),
            entity.getUsername(),
            entity.getName(),
            entity.getSurname()
        );
    }

    @Override
    public Buyer fromDtoToEntity(BuyerSummaryDTO dto) {
        Buyer entity = new Buyer();
        entity.setId(dto.id());
        entity.setUsername(dto.username());
        entity.setName(dto.name());
        entity.setSurname(dto.surname());
        return entity;
    }
    
}

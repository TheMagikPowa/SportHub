package com.generation.SportHub.converters;

import org.springframework.stereotype.Service;

import com.generation.SportHub.dto.BuyerDTO;
import com.generation.SportHub.entity.Buyer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuyerConverter implements GenericConverter<BuyerDTO, Buyer> {

    private final EventConverter ec;
    private final EventAnswerConverter eac;
    private final AddressConverter ac;

    @Override
    public BuyerDTO fromEntityToDto(Buyer entity) {
        
        return new BuyerDTO(
            entity.getId(),
            entity.getEmail(),
            entity.getUsername(),
            entity.getName(),
            entity.getSurname(),
            entity.getDob() != null ? entity.getDob() : null,
            entity.getGender(),
            entity.getRole(),
            entity.isActive(),
            entity.getListEvents().stream().map(ec::fromEntityToDto).toList(),
            entity.getEventAnswers().stream().map(eac::fromEntityToDto).toList(),
            entity.getAddresses().stream().map(ac::fromEntityToDto).toList()           
        );
    }

    @Override
    public Buyer fromDtoToEntity(BuyerDTO dto) {
        Buyer entity = new Buyer();
        entity.setUsername(dto.username());
        entity.setName(dto.name());
        entity.setSurname(dto.surname());
        entity.setDob(dto.dob() != null ? dto.dob() : null);
        entity.setGender(dto.gender());
        entity.setRole(dto.role());
        entity.setListEvents(dto.listEvents().stream().map(ec::fromDtoToEntity).toList());
        entity.setEventAnswers(dto.eventAnswers().stream().map(eac::fromDtoToEntity).toList());
        entity.setAddresses(dto.addresses().stream().map(ac::fromDtoToEntity).toList());
        return entity;
    }
    
}

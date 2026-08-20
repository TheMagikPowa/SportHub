package com.generation.SportHub.converters;

import org.springframework.stereotype.Service;

import com.generation.SportHub.dto.AddressDTO;
import com.generation.SportHub.dto.BuyerSummaryDTO;
import com.generation.SportHub.entity.Address;
import com.generation.SportHub.entity.Buyer;



@Service
public class AddressConverter implements GenericConverter<AddressDTO, Address>{

    @Override
    public AddressDTO fromEntityToDto(Address entity) {
        Buyer buyer = entity.getBuyer();
        BuyerSummaryDTO buyerSummaryDto = new BuyerSummaryDTO(
            buyer.getId(),
            buyer.getUsername(),
            buyer.getName(),
            buyer.getSurname()
        );
        return new AddressDTO(
            entity.getId(),
            buyerSummaryDto,
            entity.getCountry(),
            entity.getProvince(),
            entity.getStreet(),
            entity.getStreetNumber(),
            entity.getPostal_code(),
            entity.getPhoneNumber()
        );
    }

    @Override
    public Address fromDtoToEntity(AddressDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromDtoToEntity'");
    }
    
}

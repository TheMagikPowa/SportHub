package com.generation.SportHub.dto;


public record AddressDTO (
    Long id,
    BuyerSummaryDTO buyer,
    String country,
    String province,
    String street,
    String streetNumber,
    String postal_code,
    Long phoneNumber
) implements GenericDTO {
    
}

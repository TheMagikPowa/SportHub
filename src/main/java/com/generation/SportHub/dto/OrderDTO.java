package com.generation.SportHub.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.generation.SportHub.entity.enums.OrderStatus;

public record OrderDTO(
    Long id,
    BuyerDTO buyer,
    Instant purchaseDate,
    BigDecimal total,
    Integer discountPercent,
    OrderStatus status
) implements GenericDTO {
    
}

package com.generation.SportHub.converters;

import org.springframework.stereotype.Service;

import com.generation.SportHub.dto.OrderDTO;
import com.generation.SportHub.entity.Order;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderConverter implements GenericConverter<OrderDTO, Order> {

    private final BuyerConverter bc;

    @Override
    public OrderDTO fromEntityToDto(Order entity) {
        if (entity == null) {
            return null;
        }
        return new OrderDTO(
            entity.getId(),
            bc.fromEntityToDto(entity.getBuyer()),
            entity.getPurchaseDate(),
            entity.getTotal(),
            entity.getDiscountPercent(),
            entity.getStatus()
        );
    }

    @Override
    public Order fromDtoToEntity(OrderDTO dto) {
        if (dto == null) {
            return null;
        }
        Order order = new Order();
        order.setId(dto.id());
        order.setBuyer(bc.fromDtoToEntity(dto.buyer()));
        order.setPurchaseDate(dto.purchaseDate());
        order.setTotal(dto.total());
        order.setDiscountPercent(dto.discountPercent());
        order.setStatus(dto.status());
        return order;
    }
    
}

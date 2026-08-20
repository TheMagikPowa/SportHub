package com.generation.SportHub.converters;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.generation.SportHub.dto.OrderHasProductDTO;
import com.generation.SportHub.entity.OrderHasProducts;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderHasProductConverter implements GenericConverter<OrderHasProductDTO, OrderHasProducts> {

     private final OrderConverter oc;
     private final ProductConverter pc;
    @Override
    public OrderHasProductDTO fromEntityToDto(OrderHasProducts entity) {
        if (entity == null) {
            return null;
        }
        return new OrderHasProductDTO(
            entity.getId(),
            oc.fromEntityToDto(entity.getOrder()),
            pc.fromEntityToDto(entity.getProduct()),
            entity.getName(),
            entity.getUnitPrice().doubleValue(),
            entity.getFinalPrice().doubleValue(),
            entity.getQuantity()
        );
    }

    @Override
    public OrderHasProducts fromDtoToEntity(OrderHasProductDTO dto) {
        if (dto == null) {
            return null;
        }
        OrderHasProducts orderHasProducts = new OrderHasProducts();
        orderHasProducts.setId(dto.id());
        orderHasProducts.setOrder(oc.fromDtoToEntity(dto.order()));
        orderHasProducts.setProduct(pc.fromDtoToEntity(dto.product()));
        orderHasProducts.setName(dto.name());
        orderHasProducts.setUnitPrice(BigDecimal.valueOf(dto.unitPrice()));
        orderHasProducts.setFinalPrice(BigDecimal.valueOf(dto.finalPrice()));
        orderHasProducts.setQuantity(dto.quantity());
        return orderHasProducts;
    }
    
}

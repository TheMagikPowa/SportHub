package com.generation.SportHub.converters;

import org.springframework.stereotype.Service;

import com.generation.SportHub.dto.CartDTO;
import com.generation.SportHub.entity.Cart;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartConverter implements GenericConverter<CartDTO, Cart> {

    private final BuyerConverter bc;

    @Override
    public CartDTO fromEntityToDto(Cart entity) {
        if (entity == null) {
            return null;
        }
        return new CartDTO(
            entity.getId(),
            bc.fromEntityToDto(entity.getBuyer()),
            entity.getCreatedAt(),
            entity.getModifiedAt()
        );
    }

    @Override
    public Cart fromDtoToEntity(CartDTO dto) {
        if (dto == null) {
            return null;
        }
        Cart cart = new Cart();
        cart.setId(dto.id());
        cart.setBuyer(bc.fromDtoToEntity(dto.buyer()));
        cart.setCreatedAt(dto.createdAt());
        cart.setModifiedAt(dto.modifiedAt());
        return cart;
    }
    
}

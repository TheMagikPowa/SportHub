package com.generation.SportHub.converters;

import org.springframework.stereotype.Service;

import com.generation.SportHub.dto.CartHasProductsDTO;
import com.generation.SportHub.entity.CartHasProducts;

@Service
public class CartHasProductsConverter implements GenericConverter<CartHasProductsDTO, CartHasProducts> {

    @Override
    public CartHasProductsDTO fromEntityToDto(CartHasProducts entity) {
        if (entity == null) {
            return null;
        }
        return new CartHasProductsDTO(
            entity.getId(),
            entity.getCart() != null ? entity.getCart().getId() : null,
            entity.getProduct() != null ? entity.getProduct().getId() : null,
            entity.getQuantity()
        );
    }

    @Override
    public CartHasProducts fromDtoToEntity(CartHasProductsDTO dto) {
        if (dto == null) {
            return null;
        }
        CartHasProducts entity = new CartHasProducts();
        entity.setId(dto.id());
        // Assuming you have a way to fetch Cart and Product entities by their IDs
        // You might need to inject services or repositories to fetch these entities
        // For example:
        // entity.setCart(cartService.findById(dto.cartId()));
        // entity.setProduct(productService.findById(dto.productId()));
        entity.setQuantity(dto.quantity());
        return entity;
    }
    
}

package com.generation.SportHub.converters;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.generation.SportHub.dto.ProductDTO;
import com.generation.SportHub.entity.Product;
import com.generation.SportHub.entity.enums.AgeCategory;
import com.generation.SportHub.entity.enums.ProductGender;

@Service
public class ProductConverter implements GenericConverter<ProductDTO, Product> {

    @Override
    public ProductDTO fromEntityToDto(Product entity) {
        if (entity == null) {
            return null;
        }
        return new ProductDTO(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getQuantity(),
            entity.getPrice().doubleValue(),
            entity.getPGender().name(),
            entity.getACategory().name()
        );
    }

    @Override
    public Product fromDtoToEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }
        Product product = new Product();
        product.setId(dto.id());
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setQuantity(dto.quantity());
        product.setPrice(BigDecimal.valueOf(dto.price()));
        product.setPGender(ProductGender.valueOf(dto.pGender()));
        product.setACategory(AgeCategory.valueOf(dto.aCategory()));
        return product;
    }
    
}

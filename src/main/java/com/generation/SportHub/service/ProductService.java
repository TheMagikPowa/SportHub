package com.generation.SportHub.service;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.generation.SportHub.converters.ProductConverter;
import com.generation.SportHub.dto.ProductDTO;
import com.generation.SportHub.entity.Product;
import com.generation.SportHub.repository.ProductRepository;

public class ProductService extends GenericService<Long, Product, ProductDTO, ProductConverter, ProductRepository> {
    
    public ProductService (ProductRepository pr, ProductConverter pc, ApplicationContext ac){
        super(pr, pc, ac);
    }

    @Override
    public Product construct(Map<String, String> params) {
        Product p= getContext().getBean(Product.class, params);
        return p;
    }
}

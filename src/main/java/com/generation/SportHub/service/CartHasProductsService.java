package com.generation.SportHub.service;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.generation.SportHub.converters.CartHasProductsConverter;
import com.generation.SportHub.dto.CartHasProductsDTO;
import com.generation.SportHub.entity.CartHasProducts;
import com.generation.SportHub.repository.CartHasProductsRepository;

public class CartHasProductsService extends GenericService<Long, CartHasProducts, CartHasProductsDTO, CartHasProductsConverter, CartHasProductsRepository> {
    
    public CartHasProductsService(CartHasProductsRepository chpr, CartHasProductsConverter chpc, ApplicationContext ac) {
        super(chpr, chpc, ac);
    }

    @Override
    public CartHasProducts construct(Map<String, String> params) {
        CartHasProducts chp= getContext().getBean(CartHasProducts.class, params);
        return chp;
    }
}

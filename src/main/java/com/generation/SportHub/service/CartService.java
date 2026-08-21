package com.generation.SportHub.service;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.generation.SportHub.converters.CartConverter;
import com.generation.SportHub.dto.CartDTO;
import com.generation.SportHub.entity.Cart;
import com.generation.SportHub.repository.CartRepository;

public class CartService extends GenericService<Long, Cart, CartDTO, CartConverter, CartRepository> {

    public CartService(CartRepository cr, CartConverter cc, ApplicationContext ac){
        super(cr, cc, ac);
    }

    @Override
    public Cart construct(Map<String, String> params) {
        Cart c= getContext().getBean(Cart.class, params);
        return c;
    }
    
}

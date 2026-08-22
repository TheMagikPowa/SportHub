package com.generation.SportHub.service;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.generation.SportHub.converters.OrderHasProductConverter;
import com.generation.SportHub.dto.OrderHasProductDTO;
import com.generation.SportHub.entity.OrderHasProducts;
import com.generation.SportHub.repository.OrderHasProductsRepository;

@Service
public class OrderHasProductsService extends GenericService<Long, OrderHasProducts, OrderHasProductDTO, OrderHasProductConverter, OrderHasProductsRepository> {
    
    public OrderHasProductsService(OrderHasProductsRepository ohpr, OrderHasProductConverter ohpc, ApplicationContext ac){
        super(ohpr, ohpc, ac);
    }

    @Override
    public OrderHasProducts construct(Map<String, String> params) {
        OrderHasProducts ohp= getContext().getBean(OrderHasProducts.class, params);
        return ohp;
    }
}

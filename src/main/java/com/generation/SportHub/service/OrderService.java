package com.generation.SportHub.service;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.generation.SportHub.converters.OrderConverter;
import com.generation.SportHub.dto.OrderDTO;
import com.generation.SportHub.entity.Order;
import com.generation.SportHub.repository.OrderRepository;

public class OrderService extends GenericService<Long, Order, OrderDTO, OrderConverter, OrderRepository> {

    public OrderService(OrderRepository or, OrderConverter oc, ApplicationContext ac){
        super(or, oc, ac);
    }

    @Override
    public Order construct(Map<String, String> params) {
        Order o= getContext().getBean(Order.class, params);
        return o;
    }
    
}

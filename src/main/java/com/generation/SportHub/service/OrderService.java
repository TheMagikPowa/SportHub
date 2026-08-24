package com.generation.SportHub.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.generation.SportHub.converters.OrderConverter;
import com.generation.SportHub.dto.OrderDTO;
import com.generation.SportHub.entity.Buyer;
import com.generation.SportHub.entity.Order;
import com.generation.SportHub.entity.enums.OrderStatus;
import com.generation.SportHub.repository.OrderRepository;

@Service
public class OrderService extends GenericService<Long, Order, OrderDTO, OrderConverter, OrderRepository> {

    private final OrderRepository oRepo;

    public OrderService(OrderRepository or, OrderConverter oc, ApplicationContext ac){
        super(or, oc, ac);
        this.oRepo=or;
    }

    @Override
    public Order construct(Map<String, String> params) {
        Order o= getContext().getBean(Order.class, params);
        return o;
    }

    public List<Order> getAllOrders() {
        return oRepo.findAll();
    }
    
    public Order getOrderById(Long id) throws Exception {
        return oRepo.findById(id)
                .orElseThrow(() -> new Exception("Order not found"));
    }
    public List<Order> getOrdersByBuyer(Buyer buyer) {
        return oRepo.findByBuyer(buyer);
    }

    public Order createOrder(Buyer buyer, BigDecimal total, Integer discountPercent) {
        Order order = new Order();
        order.setBuyer(buyer);
        order.setPurchaseDate(Instant.now()); 
        order.setTotal(total);
        order.setDiscountPercent(discountPercent != null ? discountPercent : 0);
        order.setStatus(OrderStatus.PROCESSING); //impostato come stato iniziale 

        return oRepo.save(order);
    }
    public Order updateOrderStatus(Long id, OrderStatus newStatus) throws Exception {
        Order order = getOrderById(id);
        order.setStatus(newStatus);
        return oRepo.save(order);
    }
}

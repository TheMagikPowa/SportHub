package com.generation.SportHub.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.generation.SportHub.converters.OrderConverter;
import com.generation.SportHub.dto.OrderDTO;
import com.generation.SportHub.entity.Buyer;
import com.generation.SportHub.entity.CartHasProducts;
import com.generation.SportHub.entity.Order;
import com.generation.SportHub.entity.OrderHasProducts;
import com.generation.SportHub.entity.enums.OrderStatus;
import com.generation.SportHub.repository.BuyerRepository;
import com.generation.SportHub.repository.OrderHasProductsRepository;
import com.generation.SportHub.repository.OrderRepository;

@Service
public class OrderService extends GenericService<Long, Order, OrderDTO, OrderConverter, OrderRepository> {

    private final OrderRepository oRepo;
    private final BuyerRepository buyerRepository;
    private final CartService cartService;
    private final OrderHasProductsRepository orderHasProductsRepository;

    public OrderService(OrderRepository or, OrderConverter oc, ApplicationContext ac, 
                        BuyerRepository buyerRepository, CartService cartService,
                        OrderHasProductsRepository orderHasProductsRepository){
        super(or, oc, ac);
        this.oRepo = or;
        this.buyerRepository = buyerRepository;
        this.cartService = cartService;
        this.orderHasProductsRepository = orderHasProductsRepository;
    }

    @Override
    public Order construct(Map<String, String> params) {
        Order o = getContext().getBean(Order.class, params);
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

    public List<Order> getOrdersByBuyerId(Long buyerId) throws Exception {
        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> new Exception("Buyer not found"));
        return oRepo.findByBuyer(buyer);
    }
    @Transactional
    public Order createOrder(Buyer buyer, BigDecimal total, Integer discountPercent) {
        Order order = new Order();
        order.setBuyer(buyer);
        order.setPurchaseDate(Instant.now()); 
        order.setTotal(total);
        order.setDiscountPercent(discountPercent != null ? discountPercent : 0);
        order.setStatus(OrderStatus.PROCESSING); // Stato iniziale 

        return oRepo.save(order);
    }
    @Transactional
    public Order updateOrderStatus(Long id, OrderStatus newStatus) throws Exception {
        Order order = getOrderById(id);
        order.setStatus(newStatus);
        return oRepo.save(order);
    }

    @Transactional
    public Order createOrderFromCart(Long buyerId, String address, String recipient, String shippingType, String paymentMethod) throws Exception {
        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> new Exception("Buyer not found"));
        
        // Recupera gli elementi dal carrello tramite l'entitÃ  esistente CartHasProducts
        List<CartHasProducts> cartItems = cartService.getCartItems(buyerId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new Exception("Cannot create order from an empty cart.");
        }
        BigDecimal total = BigDecimal.ZERO;
        for (CartHasProducts chp : cartItems) {
            BigDecimal itemSub = chp.getProduct().getPrice().multiply(BigDecimal.valueOf(chp.getQuantity()));
            total = total.add(itemSub);
        }
        if ("premium".equalsIgnoreCase(shippingType)) {
            total = total.add(new BigDecimal("10.00"));
        } else {
            total = total.add(new BigDecimal("5.00"));
        }
        Order savedOrder = createOrder(buyer, total, 0);
        for (CartHasProducts chp : cartItems) {
            OrderHasProducts ohp = new OrderHasProducts();
            ohp.setOrder(savedOrder);
            ohp.setProduct(chp.getProduct());
            ohp.setName(chp.getProduct().getName());
            ohp.setUnitPrice(chp.getProduct().getPrice());
            
            BigDecimal finalPrice = chp.getProduct().getPrice().multiply(BigDecimal.valueOf(chp.getQuantity()));
            ohp.setFinalPrice(finalPrice);
            
            ohp.setQuantity(chp.getQuantity());

            orderHasProductsRepository.save(ohp);
        }

        //  Svuota il carrello a transazione completata
        cartService.clearCart(buyerId);

        return savedOrder;
    }
}
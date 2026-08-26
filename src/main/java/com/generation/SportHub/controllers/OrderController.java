package com.generation.SportHub.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.generation.SportHub.entity.Buyer;
import com.generation.SportHub.entity.Order;
import com.generation.SportHub.entity.enums.OrderStatus;
import com.generation.SportHub.service.BuyerService;
import com.generation.SportHub.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService oService;
    private final BuyerService bService;

    public OrderController(OrderService oService, BuyerService bService) {
        this.oService = oService;
        this.bService = bService;
    }

    @GetMapping
    public String listUserOrders(Model model, Principal principal) {
        try {
            String email = principal.getName();
            Buyer buyer = bService.getBuyerByUsername(email);
            
            
            List<Order> orders = oService.getOrdersByBuyerId(buyer.getId());
            model.addAttribute("orders", orders);
            
            return "order/order"; 
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error/errorPage";
        }
    } 

    @GetMapping("/{id}")
    public String viewOrderDetail(@PathVariable Long id, Model model) {
        try {
            Order order = oService.getOrderById(id);
            model.addAttribute("order", order);
            return "order/orderDetail"; 
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error/errorPage";
        }
    }

    @PostMapping("/update-status/{id}")
    public String updateStatus(@PathVariable Long id, 
                               @RequestParam OrderStatus status, 
                               RedirectAttributes redirectAttributes) {
        try {
            oService.updateOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Order status updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Impossibile aggiornare lo stato: " + e.getMessage());
        }
        return "redirect:/orders/" + id;
    }
}

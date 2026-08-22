package com.generation.SportHub.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.generation.SportHub.entity.Order;
import com.generation.SportHub.entity.enums.OrderStatus;
import com.generation.SportHub.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService oService;

    public OrderController(OrderService oService) {
        this.oService = oService;
    }
   @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = oService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order-list"; 
        //TODO controllare nome pagine 
    } 
    @GetMapping("/{id}")
    public String viewOrderDetail(@PathVariable Long id, Model model) {
        try {
            Order order = oService.getOrderById(id);
            model.addAttribute("order", order);
            return "ordeDetail"; 
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
        
        
    } catch (Exception e) {
//vista la presenza del redirect (che genera nuova richiesta http) bsiogna usare flash attributes
        redirectAttributes.addFlashAttribute("errorMessage", "Impossibile aggiornare lo stato: " + e.getMessage());
    }
    // Reindirizza alla pagina di dettaglio dell'ordine
    return "redirect:/orders/" + id;
}
}

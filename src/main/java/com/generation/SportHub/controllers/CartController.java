package com.generation.SportHub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.generation.SportHub.entity.Cart;
import com.generation.SportHub.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cService;

    public CartController(CartService cartService) {
        this.cService = cartService;
    }

    @GetMapping("/{buyerId}")
    public String viewCart(@PathVariable Long buyerId, Model model) {
        try {
            Cart cart = cService.getCartByBuyerId(buyerId);
            // Il service dovrebbe anche recuperare la lista dei "CartHasProducts" associati a questo carrello
            model.addAttribute("cart", cart);
            model.addAttribute("cartItems", cService.getCartItems(buyerId));
            
            return "cart/cart-view"; 
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error/errorPage";
        }
    }

    @PostMapping("/{buyerId}/remove/{productId}")
    public String removeProductFromCart(@PathVariable Long buyerId, 
                                        @PathVariable Long productId,
                                        RedirectAttributes redirectAttributes) {
        try {
            cService.removeProductFromCart(buyerId, productId);
            redirectAttributes.addFlashAttribute("successMessage", "The product has been removed");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/cart/" + buyerId;
    }

    @PostMapping("/{buyerId}/add")
    public String addProductToCart(@PathVariable Long buyerId, 
                                   @RequestParam Long productId, 
                                   @RequestParam Integer quantity,
                                   RedirectAttributes redirectAttributes) {
        try {
            cService.addProductToCart(buyerId, productId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Product succesfully added to the cart!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/cart/" + buyerId;
    }
}

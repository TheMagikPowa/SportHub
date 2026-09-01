package com.generation.SportHub.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.generation.SportHub.entity.Cart;
import com.generation.SportHub.entity.Person;
import com.generation.SportHub.repository.PersonRepository;
import com.generation.SportHub.service.CartService;
import com.generation.SportHub.service.OrderService;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cService;
    private final OrderService oService;
    private final PersonRepository pRepo;

    public CartController(CartService cartService, OrderService orderService, PersonRepository pRepo) {
        this.cService = cartService;
        this.oService = orderService;
        this.pRepo = pRepo;
    }

    private Long getLoggedInBuyerId(Principal principal) {
        String email = principal.getName();
        Person person = pRepo.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return person.getId(); 
    }

    @GetMapping
    public String viewCart(Principal principal, Model model) {
        try {
            Long buyerId = getLoggedInBuyerId(principal);

            Cart cart = cService.getOrCreateCart(buyerId);
            var cartItems = cService.getCartItems(buyerId);
            
            model.addAttribute("cart", cart);
            model.addAttribute("cartItems", cartItems);
            
            java.math.BigDecimal totalAmount = java.math.BigDecimal.ZERO;
            if (cartItems != null) {
                for (var item : cartItems) {
                    java.math.BigDecimal itemTotal = item.getProduct().getPrice()
                        .multiply(java.math.BigDecimal.valueOf(item.getQuantity()));
                    totalAmount = totalAmount.add(itemTotal);
                }
            }
            
            model.addAttribute("totalAmount", totalAmount);
            
            return "cart/cart"; 
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error/errorPage";
        }
    }

    @PostMapping("/remove/{productId}")
    public String removeProductFromCart(@PathVariable Long productId, 
                                        Principal principal,
                                        RedirectAttributes redirectAttributes) {
        try {
            Long buyerId = getLoggedInBuyerId(principal);
            cService.removeProductFromCart(buyerId, productId);
            redirectAttributes.addFlashAttribute("successMessage", "The product has been removed");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/add")
    public String addProductToCart(
            Principal principal,
            @RequestParam Long productId, 
            @RequestParam(defaultValue = "1") Integer quantity,
            RedirectAttributes redirectAttributes) {
        try {
            Long buyerId = getLoggedInBuyerId(principal);
            cService.addProductToCart(buyerId, productId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Product successfully added to the cart!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String viewCheckoutPage(Principal principal, Model model) {
        try {
            Long buyerId = getLoggedInBuyerId(principal);

            Cart cart = cService.getOrCreateCart(buyerId);
            var cartItems = cService.getCartItems(buyerId);
            
            java.math.BigDecimal totalAmount = java.math.BigDecimal.ZERO;
            if (cartItems != null) {
                for (var item : cartItems) {
                    java.math.BigDecimal itemTotal = item.getProduct().getPrice()
                        .multiply(java.math.BigDecimal.valueOf(item.getQuantity()));
                    totalAmount = totalAmount.add(itemTotal);
                }
            }
            
            model.addAttribute("cart", cart);
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalAmount", totalAmount);
            
            return "checkout/checkout"; 
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error/errorPage";
        }
    }

    @PostMapping("/checkout")
    public String confirmCheckout(Principal principal,
                                  @RequestParam String address,
                                  @RequestParam String recipient,
                                  @RequestParam String shippingType,
                                  @RequestParam String paymentMethod,
                                  RedirectAttributes redirectAttributes) {
        try {
            Long buyerId = getLoggedInBuyerId(principal);
            
            // Converte il carrello in un ordine effettivo e pulisce il carrello
            oService.createOrderFromCart(buyerId, address, recipient, shippingType, paymentMethod);
            
            // CORRETTO: Assicura il messaggio flash ed effettua il redirect pulito al carrello
            redirectAttributes.addFlashAttribute("successMessage", "Order placed successfully! Thank you for your purchase.");
            
            return "redirect:/cart"; 
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Checkout error: " + e.getMessage());
            return "redirect:/cart/checkout";
        }
    }
}

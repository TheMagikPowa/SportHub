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

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cService;
    private final PersonRepository pRepo;

    public CartController(CartService cartService, PersonRepository pRepo) {
        this.cService = cartService;
        this.pRepo = pRepo;
    }

    @GetMapping
    public String viewCart(Principal principal, Model model) {
        try {
            // 1. Ricaviamo la mail dell'utente loggato
            String email = principal.getName();
            
            // 2. Troviamo la persona / buyer associata (adattalo in base al tuo DB/Service)
            // Supponendo che tu abbia un metodo per recuperare il carrello direttamente tramite la mail o l'id del buyer collegato:
            Person person = pRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            // NOTA: Se 'person' è un Buyer o ha un Buyer collegato, usa quel dato:
            Long buyerId = person.getId(); // O person.getBuyer().getId() in base alla tua struttura

            Cart cart = cService.getCartByBuyerId(buyerId);
            model.addAttribute("cart", cart);
            model.addAttribute("cartItems", cService.getCartItems(buyerId));
            
            return "cart/cart"; 
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

    @PostMapping("/add")
    public String addProductToCart(
            Principal principal,
            @RequestParam Long productId, 
            @RequestParam(defaultValue = "1") Integer quantity,
            RedirectAttributes redirectAttributes) {
        try {
            // Ricaviamo l'utente loggato in modo sicuro dalla sessione
            String email = principal.getName();
            Person person = pRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            // Assumendo che l'id della persona corrisponda al buyerId (o tramite la relazione corretta)
            Long buyerId = person.getId(); // Oppure person.getBuyer().getId()

            cService.addProductToCart(buyerId, productId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Product successfully added to the cart!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        
        // Reindirizza al carrello senza ID nell'URL
        return "redirect:/cart";
    }
    @PostMapping("/{buyerId}/checkout")
    public String confirmCheckout(@PathVariable Long buyerId,
                                  @RequestParam String address,
                                  @RequestParam String recipient,
                                  @RequestParam String shippingType,
                                  @RequestParam String paymentMethod,
                                  RedirectAttributes redirectAttributes) {
        try {
            
            redirectAttributes.addFlashAttribute("successMessage", "Order placed successfully! Thank you for your purchase.");
            return "redirect:/dashboard"; 
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Checkout error: " + e.getMessage());
            return "redirect:/cart/" + buyerId;
        }
    }
}

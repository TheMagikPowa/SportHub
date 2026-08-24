package com.generation.SportHub.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.generation.SportHub.entity.Address;
import com.generation.SportHub.entity.Buyer;
import com.generation.SportHub.service.BuyerService;

@Controller
@RequestMapping("/profile")

public class BuyerController {

    private final BuyerService bService;

    public BuyerController(BuyerService buyerService) {
        this.bService = buyerService;
    }
@GetMapping("/{id}")
    public String viewBuyerProfile(@PathVariable Long id, Model model) {
        try {
            Buyer buyer = bService.getBuyerById(id);
            List<Address> addresses = bService.getAddressesByBuyer(id);
            
            model.addAttribute("buyer", buyer);
            model.addAttribute("addresses", addresses);
            
            return "buyer/profile"; // Vista HTML del profilo buyer con rubrica indirizzi
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errorPage";
        }
    }
    //aggiunta nuovo indirizzo (per ora ipotesi tramite form, da verificare)
@GetMapping("/{buyerId}/address/new")
    public String showAddAddressForm(@PathVariable Long buyerId, Model model) {
        model.addAttribute("buyerId", buyerId);
        model.addAttribute("address", new Address());
        return "buyer/address-form"; 
    }
@PostMapping("/{buyerId}/address/save")
    public String saveAddress(@PathVariable Long buyerId, @ModelAttribute("address") Address address) {
        try {
            bService.addAddressToBuyer(buyerId, address);
        } catch (Exception e) {
            // TODO da completare
        }
        return "redirect:/buyer/profile/" + buyerId;
    }

}

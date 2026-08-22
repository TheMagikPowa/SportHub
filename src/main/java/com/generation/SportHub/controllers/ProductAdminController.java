package com.generation.SportHub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.generation.SportHub.dto.ProductDTO;
import com.generation.SportHub.service.ProductService;

@Controller
@RequestMapping("/admin/products")
public class ProductAdminController {

    private final ProductService pService; 
    
    public ProductAdminController(ProductService productService) {
        this.pService = productService;
    }

    //funzioni che devono essere disponibili: 
    // aggiungi prodotto al catalogo
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("productDTO") ProductDTO productDTO) {
        pService.createProduct(productDTO);
        return "redirect:/products";
    } //TODO controllare rimandi alle pagine e modelAttribute per corrispondenza con front
   

    //elimina prodotto al catalogo

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        try {
            pService.deleteProduct(id);
        } catch (Exception e) {
            // Gestione dell'errore di eliminazione
        }
        return "redirect:/products";
    }
    //aggiorna prodotto del catalogo
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute("productDTO") ProductDTO productDTO) {
        try {
            pService.updateProduct(id, productDTO);
            return "redirect:/products";
        } catch (Exception e) {
            return "redirect:/admin/products/edit/" + id + "?error";
        }
    }

    // quantità dei prodotti ?????

}

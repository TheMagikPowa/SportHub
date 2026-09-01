package com.generation.SportHub.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.generation.SportHub.entity.Product;
import com.generation.SportHub.entity.enums.AgeCategory;
import com.generation.SportHub.entity.enums.ProductGender;
import com.generation.SportHub.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    //funzioni del buyer rispetto i prodotti 


    private final ProductService pService;

    public ProductController (ProductService pService) {
        this.pService= pService;
    }

    @GetMapping("/store")
public String showStore(Model model) {
    // Recupera tutti i prodotti dal database tramite il tuo Service o Repository
    List<Product> products = pService.getAllProducts(); 
   
        if (products == null) {
            products = new ArrayList<>(); // Evita il null pointer
        }

    model.addAttribute("products", products);
    return "store/store"; // Nome della tua pagina HTML dello store
}

    
//funzioni pensate per utenti/buyer: lista prodotti (con o senza filtri), scheda singola 

    @GetMapping
    public String listProducts (@RequestParam(required = false) String name,@RequestParam(required = false) ProductGender pGender,
                @RequestParam(required = false) AgeCategory aCategory,
                Model model) {
            
            List<Product> products = pService.searchAndFilterProducts(name, pGender, aCategory);
            
            model.addAttribute("products", products);
            model.addAttribute("selectedName", name);
            model.addAttribute("selectedGender", pGender);
            model.addAttribute("selectedCategory", aCategory);

        return "product-list";   
    }

    @GetMapping("/{id}")
    public String viewSingleProduct (@PathVariable Long id, Model model) {
        try {
            Product product = pService.getProductById(id);
            model.addAttribute("product", product);
            return "product/product";
   

        }catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error/errorPage";
        }
    }
}

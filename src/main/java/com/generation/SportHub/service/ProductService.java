package com.generation.SportHub.service;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.generation.SportHub.converters.ProductConverter;
import com.generation.SportHub.dto.ProductDTO;
import com.generation.SportHub.entity.Product;
import com.generation.SportHub.entity.enums.AgeCategory;
import com.generation.SportHub.entity.enums.ProductGender;
import com.generation.SportHub.repository.ProductRepository;

@Service
public class ProductService extends GenericService<Long, Product, ProductDTO, ProductConverter, ProductRepository> {
    
    private final ProductRepository pRepo; 
    public ProductService (ProductRepository pr, ProductConverter pc, ApplicationContext ac){
        super(pr, pc, ac);
        this.pRepo = pr;
    }

    @Override
    public Product construct(Map<String, String> params) {
        Product p= getContext().getBean(Product.class, params);
        return p;
    }
    public List<Product> getAllProducts() {
            return pRepo.findAll();
        }

    public Product getProductById(Long id) throws Exception {
        return pRepo.findById(id).orElseThrow(() -> new Exception("Product not found :("));
    }
    public List<Product> searchAndFilterProducts(String name, ProductGender pGender, AgeCategory aCategory) {
        if (name != null && !name.trim().isEmpty()) {
            return pRepo.findByNameContainingIgnoreCase(name);
        } else if (pGender != null) {
            return pRepo.findByPGender(pGender);
        } else if (aCategory != null) {
            return pRepo.findByACategory(aCategory);
        } else {
            return pRepo.findAll();
        }
    }
//azioni dell'admin
    public Product createProduct(ProductDTO productDTO) {
        Product p = new Product();
        p.setName(productDTO.name());
        p.setPrice(productDTO.price());
        p.setQuantity(productDTO.quantity());
        p.setDescription(productDTO.description());
        p.setPGender(productDTO.pGender());
        p.setACategory(productDTO.aCategory());
        p.setCategory(productDTO.category());
        
        return pRepo.save(p);
    }

    public Product updateProduct(Long id, ProductDTO productDTO) throws Exception {
        Product p = pRepo.findById(id).orElseThrow(() -> new Exception("Product not found "));

        p.setName(productDTO.name());
        p.setPrice(productDTO.price());
        p.setQuantity(productDTO.quantity());
        p.setDescription(productDTO.description());
        p.setPGender(productDTO.pGender());
        p.setACategory(productDTO.aCategory());
        p.setCategory(productDTO.category());

        return pRepo.save(p);
    }

    public void deleteProduct(Long id) throws Exception {
        if (!pRepo.existsById(id)) {
            throw new Exception("Impossibile eliminare: prodotto non trovato.");
        }
        pRepo.deleteById(id);
    }
}

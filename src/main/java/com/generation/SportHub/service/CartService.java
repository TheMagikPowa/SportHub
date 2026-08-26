package com.generation.SportHub.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.generation.SportHub.converters.CartConverter;
import com.generation.SportHub.dto.CartDTO;
import com.generation.SportHub.entity.Buyer;
import com.generation.SportHub.entity.Cart;
import com.generation.SportHub.entity.CartHasProducts;
import com.generation.SportHub.entity.Product;
import com.generation.SportHub.repository.BuyerRepository;
import com.generation.SportHub.repository.CartHasProductsRepository;
import com.generation.SportHub.repository.CartRepository;
import com.generation.SportHub.repository.ProductRepository;

@Service
public class CartService extends GenericService<Long, Cart, CartDTO, CartConverter, CartRepository> {

    private final CartRepository cRepo;
    private final CartHasProductsRepository cartHasProductsRepo;
    private final BuyerRepository bRepo;
    private final ProductRepository pRepo;

    public CartService(CartRepository cr, CartConverter cc, ApplicationContext ac, ProductRepository pRepo, BuyerRepository bRepo, CartHasProductsRepository cartHasProductsRepo){
        super(cr, cc, ac);
        this.cRepo=cr;
        this.bRepo = bRepo;
        this.pRepo = pRepo;
        this.cartHasProductsRepo = cartHasProductsRepo;

    }

    @Override
    public Cart construct(Map<String, String> params) {
        Cart c= getContext().getBean(Cart.class, params);
        return c;
    }

    public Cart getOrCreateCart(Long buyerId) throws Exception {
        return cRepo.findById(buyerId).orElseGet(() -> {
        
            Buyer buyer = bRepo.findById(buyerId).orElseThrow(() -> new RuntimeException("Buyer not found"));
            
            Cart newCart = new Cart();
            newCart.setBuyer(buyer);
            newCart.setCreatedAt(Instant.now());
            newCart.setModifiedAt(Instant.now());
            
            return cRepo.save(newCart);
        });
    }

    public Cart getCartByBuyerId(Long buyerId) throws Exception {
        return cRepo.findById(buyerId).orElseThrow(() -> new Exception("Cart not found for buyer ID:" +buyerId));
    }
    
    //lista prodotti nel carrello
    public List<CartHasProducts> getCartItems(Long buyerId) throws Exception {
      
        Cart cart = getOrCreateCart(buyerId);
        return cartHasProductsRepo.findByCartId(cart.getId());
    }

    public void addProductToCart(Long buyerId, Long productId, Integer quantity) throws Exception {
        
        Cart cart = getOrCreateCart(buyerId);
        Product product = pRepo.findById(productId).orElseThrow(() -> new Exception("Product not found"));

        // Aggiorna la data di modifica del carrello
        cart.setModifiedAt(Instant.now());
        cRepo.save(cart);

        // Controlla se il prodotto è già nel carrello
        CartHasProducts elemento = cartHasProductsRepo.findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (elemento != null) {
           
            elemento.setQuantity(elemento.getQuantity() + quantity);
            cartHasProductsRepo.save(elemento);
        } else {
         
            CartHasProducts newProduct = new CartHasProducts();
            newProduct.setCart(cart);
            newProduct.setProduct(product);
            newProduct.setQuantity(quantity);
            cartHasProductsRepo.save(newProduct);
        }
    }

        public void removeProductFromCart(Long buyerId, Long productId) throws Exception {
        Cart cart = getCartByBuyerId(buyerId);

        CartHasProducts item = cartHasProductsRepo.findByCartIdAndProductId(cart.getId(), productId).orElseThrow(() -> new Exception("Product not present in the cart "));

        cartHasProductsRepo.delete(item);

        cart.setModifiedAt(Instant.now());
        cRepo.save(cart);
    }

    public void clearCart(Long buyerId) throws Exception {
    Cart cart = getCartByBuyerId(buyerId);

    List<CartHasProducts> cartItems =
            cartHasProductsRepo.findByCartId(cart.getId());

    cartHasProductsRepo.deleteAll(cartItems);

    cart.setModifiedAt(Instant.now());
    cRepo.save(cart);
}

    
}

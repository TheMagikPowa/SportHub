package com.generation.SportHub.service;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.generation.SportHub.converters.BuyerConverter;
import com.generation.SportHub.dto.BuyerDTO;
import com.generation.SportHub.entity.Address;
import com.generation.SportHub.entity.Buyer;
import com.generation.SportHub.repository.AddressRepository;
import com.generation.SportHub.repository.BuyerRepository;

@Service
public class BuyerService extends GenericService<Long, Buyer, BuyerDTO, BuyerConverter, BuyerRepository> {

    private final BuyerRepository bRepo;
    private final AddressRepository aRepo;

    public BuyerService(BuyerRepository br, BuyerConverter bc, ApplicationContext ac, AddressRepository addressRepository) {
        super(br, bc, ac);
        this.bRepo=br;
        this.aRepo = addressRepository;
    }
    @Override
    public Buyer construct(Map<String, String> params) {
        Buyer b= getContext().getBean(Buyer.class, params);
        return b;
    }
    public Buyer getBuyerById(Long id) throws Exception {
        return bRepo.findById(id)
                .orElseThrow(() -> new Exception("Buyer non trovato a sistema."));
    }

   public List<Address> getAddressesByBuyer(Long buyerId) throws Exception {
        Buyer buyer = bRepo.findById(buyerId).orElseThrow(() -> new Exception("User not found"));
        return buyer.getAddresses();
    }

    public Address addAddressToBuyer(Long buyerId, Address newAddress) throws Exception {
        Buyer buyer = bRepo.findById(buyerId).orElseThrow(() -> new Exception("user not found"));
        
        // Associa l'indirizzo al buyer sfruttando la relazione
        newAddress.setBuyer(buyer);
        
        return aRepo.save(newAddress);
    }

    public Buyer getBuyerByUsername(String email) {
    return bRepo.findByEmailIgnoreCase(email)
            .orElseThrow(() ->
                    new RuntimeException("Buyer non trovato a sistema."));
}

  
}

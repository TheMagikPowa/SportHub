package com.generation.SportHub.service;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.generation.SportHub.converters.BuyerConverter;
import com.generation.SportHub.dto.BuyerDTO;
import com.generation.SportHub.entity.Buyer;
import com.generation.SportHub.repository.BuyerRepository;

public class BuyerService extends GenericService<Long, Buyer, BuyerDTO, BuyerConverter, BuyerRepository> {

    public BuyerService(BuyerRepository br, BuyerConverter bc, ApplicationContext ac) {
        super(br, bc, ac);
    }
    @Override
    public Buyer construct(Map<String, String> params) {
        Buyer b= getContext().getBean(Buyer.class, params);
        return b;
    }
    
}

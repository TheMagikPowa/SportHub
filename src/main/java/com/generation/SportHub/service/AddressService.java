package com.generation.SportHub.service;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.generation.SportHub.converters.AddressConverter;
import com.generation.SportHub.dto.AddressDTO;
import com.generation.SportHub.entity.Address;
import com.generation.SportHub.repository.AddressRepository;

@Service
public class AddressService extends GenericService<Long, Address, AddressDTO, AddressConverter, AddressRepository> {

    public AddressService(AddressRepository ar, AddressConverter acc, ApplicationContext ac) {
        super(ar, acc, ac);
    }

    @Override
    public Address construct(Map<String, String> params) {
        Address a= getContext().getBean(Address.class, params);
        return a;
    }
    
}

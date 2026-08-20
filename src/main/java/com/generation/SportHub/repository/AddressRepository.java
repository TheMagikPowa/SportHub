package com.generation.SportHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.SportHub.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    
}

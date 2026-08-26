package com.generation.SportHub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.SportHub.entity.Buyer;

public interface BuyerRepository extends JpaRepository<Buyer,Long> {

Optional<Buyer> findByEmail(String email);

Optional<Buyer> findByEmailIgnoreCase(String email);
}

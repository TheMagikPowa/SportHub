package com.generation.SportHub.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class Cart {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToOne 
    @JoinColumn(name = "buyers_person_user_id", unique = true)
    private Buyer buyer;
    
    @Column
    private Instant createdAt;
    
    @Column(name = "modified_at")
    private Instant modifiedAt;
}


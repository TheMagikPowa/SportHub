package com.generation.SportHub.entity;

import java.time.Instant;

import com.generation.SportHub.entity.enums.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name="orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class Order {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyers_people_user_id")
    private Buyer buyer;

    @Column(name = "purchase_date", nullable = false)
    private Instant purchaseDate;
    
    @Column(name = "discount_percent", nullable = false)
    private Integer discountPercent;

    @Enumerated(EnumType.STRING) 
    @Column
    private OrderStatus status;

}

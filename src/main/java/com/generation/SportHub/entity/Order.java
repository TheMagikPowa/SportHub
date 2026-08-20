package com.generation.SportHub.entity;

import java.math.BigDecimal;
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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table (name="orders")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@EqualsAndHashCode(callSuper=false)
public class Order extends GenericEntity {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyers_people_user_id")
    private Buyer buyer;

    @Column(name = "purchase_date", nullable = false)
    private Instant purchaseDate;

    @Column(name= "total", nullable = false)
    private BigDecimal total;
    
    @Column(name = "discount_percent", nullable = false)
    private Integer discountPercent;

    @Enumerated(EnumType.STRING) 
    @Column
    private OrderStatus status;

}

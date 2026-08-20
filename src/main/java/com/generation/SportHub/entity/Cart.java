package com.generation.SportHub.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@EqualsAndHashCode(callSuper=false)
public class Cart extends GenericEntity {

    @Id
    @Column(name = "buyers_people_user_id")
    private Long id;

    @OneToOne 
    @MapsId
    @JoinColumn(name = "buyers_people_user_id", unique = true)
    private Buyer buyer;
    
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    
    @Column(name = "modified_at", nullable = false)
    private Instant modifiedAt;
}


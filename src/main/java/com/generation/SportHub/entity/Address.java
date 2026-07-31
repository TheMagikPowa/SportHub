package com.generation.SportHub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "addresses")

public class Address {

    @Id
    private Long buyerId;
    //da controllare

    @OneToOne @MapsId @JoinColumn(name = "buyers_people_user_id") 
    private Buyer buyer;

    @Column(nullable = false) 
    private String country;

    @Column(nullable = false) 
    private String province;

    @Column(nullable = false) 
    private String street;

    @Column(name ="street_number", nullable = false, length = 10) 
    private String streetNumber;

    @Column(name ="postal_code", nullable = false) 
    private int cap;

    @Column(name = "phone_number", nullable = false) 
    private Long phoneNumber;

   /*  buyers_people_user_id BIGINT UNSIGNED,
    country VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    province VARCHAR(2) NOT NULL,
    street VARCHAR(50) NOT NULL,
    street_number VARCHAR(10) NOT NULL,
    postal_code INT NOT NULL,
    phone_number BIGINT NOT NULL,
    FOREIGN KEY (buyers_people_user_id) REFERENCES buyers(people_user_id)
); */
}

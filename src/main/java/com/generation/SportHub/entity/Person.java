package com.generation.SportHub.entity;

import java.time.LocalDate;

import com.generation.SportHub.entity.enums.PersonGender;
import com.generation.SportHub.entity.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Entity
@Table (name ="people")

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "user_id", columnDefinition = "BIGINT UNSIGNED")
public class Person extends User{


    @Column(length = 50, unique = true)
    private String username;

    @Column(nullable = false, length=50) 
    private String name;

    @Column(nullable = false, length=50) 
    private String surname;

    @Column(nullable = false, length=50) 
    private LocalDate dob; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PersonGender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;  
   
    @PrePersist //inserito per gestire l'assegnazione di default del ruolo di buyer 
    public void initializeRole() {
        if (this.role == null) {
            this.role = Role.BUYER;
        }
}

}
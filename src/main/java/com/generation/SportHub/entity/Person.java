package com.generation.SportHub.entity;

import java.time.LocalDateTime;

import com.generation.SportHub.entity.enums.PersonGender;
import com.generation.SportHub.entity.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Entity
@Table (name ="people")

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "user_id")
public abstract class Person extends User{


    @Column(length = 50, unique = true)
    private String username;

    @Column(nullable = false, length=50) 
    private String name;

    @Column(nullable = false, length=50) 
    private String surname;

    @Column(nullable = false, length=50) 
    private LocalDateTime dob; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PersonGender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role =  Role.BUYER;  
   
  

}

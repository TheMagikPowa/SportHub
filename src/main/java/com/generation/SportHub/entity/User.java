package com.generation.SportHub.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;


@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
@Setter

public class User extends GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column (nullable = false, length = 800)
    private String password;

    
    @Column(name = "created_time", updatable = false, nullable = false)
    private Instant createTime;

    @PrePersist
    public void initializeCreationTime() {
        if (createTime == null) {
        createTime = Instant.now();
    }
    }
}

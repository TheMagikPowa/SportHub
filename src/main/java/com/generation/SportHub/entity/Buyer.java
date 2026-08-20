package com.generation.SportHub.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Entity
@Table(name ="buyers")

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor

@PrimaryKeyJoinColumn(name = "user_people_id")

public class Buyer extends Person {


    @Column(nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "buyer")
    private List<Event> listEvents;

    @OneToMany(mappedBy = "buyer")
    private List<EventAnswer> eventAnswers;

    @OneToMany(mappedBy = "buyer")
    private List<Address> addresses;
}

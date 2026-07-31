package com.generation.SportHub.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Entity
@Table(name ="buyers")

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

@PrimaryKeyJoinColumn(name = "user_people_id")

public class Buyer extends Person {

    @Column(name = "events_id")
    private Long eventsId;


    @Column(nullable = false)
    private boolean active;


    public void setActive(boolean active) { 

        this.active = active;
    }

    @OneToMany(mappedBy = "buyer")
    private List<Event> listEvents;
    

}

package com.generation.SportHub.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.generation.SportHub.entity.enums.EventType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Table (name ="events")

@Data
@EqualsAndHashCode(callSuper=false)

public class Event extends GenericEntity{

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyers_user_people_id", nullable = false) 
    private Buyer buyer;

    @Column(name = "title", nullable = false, length = 100)
    private String eventTitle;
   
   @Column(name = "text", nullable = false, length = 1000)
    private String message;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant messageTime;

    @Enumerated(EnumType.STRING) 
    @Column(nullable = false)
    private EventType type;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventAnswer> eventAnswers = new ArrayList<>();
}

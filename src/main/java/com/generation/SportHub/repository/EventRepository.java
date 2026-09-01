package com.generation.SportHub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.SportHub.entity.Buyer;
import com.generation.SportHub.entity.Event;

import com.generation.SportHub.entity.enums.EventType;

public interface EventRepository extends JpaRepository <Event, Long> {


    List<Event> findByBuyer(Buyer buyer);

    List<Event> findByEventTitleContainingIgnoreCase(String keyword);

    List<Event> findByType(EventType type);

    List<Event> findAllByOrderByMessageTimeDesc();
}

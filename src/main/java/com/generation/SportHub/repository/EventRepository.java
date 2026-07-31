package com.generation.SportHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.SportHub.entity.Event;

public interface EventRepository extends JpaRepository <Event, Long> {

}

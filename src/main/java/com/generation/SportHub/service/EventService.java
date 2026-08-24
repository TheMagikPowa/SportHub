package com.generation.SportHub.service;

import java.time.Instant;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.generation.SportHub.converters.EventConverter;
import com.generation.SportHub.dto.EventDTO;
import com.generation.SportHub.entity.Event;
import com.generation.SportHub.entity.EventAnswer;
import com.generation.SportHub.repository.EventAnswerRepository;
import com.generation.SportHub.repository.EventRepository;

import jakarta.persistence.EntityNotFoundException;



@Service
public class EventService extends GenericService<Long, Event, EventDTO, EventConverter, EventRepository> {
    
    private final EventRepository eRepo;
    private final EventAnswerRepository eventAnswerRepo;


    public EventService(EventRepository er, EventConverter ec, ApplicationContext ac,EventAnswerRepository eventAnswerRepo){
        super(er, ec, ac);
        this.eRepo=er;
        this.eventAnswerRepo= eventAnswerRepo;
    }

    @Override
    public Event construct(Map<String, String> params) {
        Event e= getContext().getBean(Event.class, params);
        return e;
    }

    public Event getEventById(Long id) {
        return eRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Event not found"));
    }

    @Transactional  
    public Event createEvent(Event event) {
        event.setMessageTime(Instant.now());
        return eRepo.save(event);
    }

    @Transactional 
    public EventAnswer addAnswerToEvent(Long eventId, EventAnswer answer) {
        Event event = getEventById(eventId);
        answer.setEvent(event);
        answer.setCreatedAt(Instant.now());
        return eventAnswerRepo.save(answer);
    }
}

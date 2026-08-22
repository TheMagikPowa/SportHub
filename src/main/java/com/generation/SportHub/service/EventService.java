package com.generation.SportHub.service;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.generation.SportHub.converters.EventConverter;
import com.generation.SportHub.dto.EventDTO;
import com.generation.SportHub.entity.Event;
import com.generation.SportHub.repository.EventRepository;

@Service
public class EventService extends GenericService<Long, Event, EventDTO, EventConverter, EventRepository> {
    
    public EventService(EventRepository er, EventConverter ec, ApplicationContext ac){
        super(er, ec, ac);
    }

    @Override
    public Event construct(Map<String, String> params) {
        Event e= getContext().getBean(Event.class, params);
        return e;
    }
}

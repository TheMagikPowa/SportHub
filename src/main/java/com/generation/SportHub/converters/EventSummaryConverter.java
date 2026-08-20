package com.generation.SportHub.converters;

import org.springframework.stereotype.Service;

import com.generation.SportHub.dto.EventSummaryDTO;
import com.generation.SportHub.entity.Event;

@Service
public class EventSummaryConverter implements GenericConverter<EventSummaryDTO, Event> {

    @Override
    public EventSummaryDTO fromEntityToDto(Event entity) {
        return new EventSummaryDTO(
            entity.getId(),
            entity.getEventTitle(),
            entity.getEventDate(),
            entity.getType()
        );
    }

    @Override
    public Event fromDtoToEntity(EventSummaryDTO dto) {
        Event entity = new Event();
        entity.setId(dto.id());
        entity.setEventTitle(dto.eventTitle());
        entity.setEventDate(dto.eventDate());
        entity.setType(dto.type());
        return entity;
    }
    
}

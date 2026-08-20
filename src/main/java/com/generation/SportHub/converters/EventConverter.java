package com.generation.SportHub.converters;

import com.generation.SportHub.dto.EventDTO;
import com.generation.SportHub.entity.Event;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventConverter implements GenericConverter<EventDTO, Event> {

    private final EventAnswerConverter eventAnswerConverter;

    @Override
    public EventDTO fromEntityToDto(Event entity) {
        return new EventDTO(
            entity.getId(),
            entity.getBuyer(),
            entity.getEventTitle(),
            entity.getMessage(),
            entity.getEventDate(),
            entity.getMessageTime(),
            entity.getType(),
            entity.getEventAnswers().stream().map(eventAnswerConverter::fromEntityToDto).toList()
        );
    }

    @Override
    public Event fromDtoToEntity(EventDTO dto) {
        Event event= new Event();
        event.setId(dto.id());
        event.setBuyer(dto.buyer());
        event.setEventTitle(dto.eventTitle());
        event.setMessage(dto.message());
        event.setEventDate(dto.eventDate());
        event.setMessageTime(dto.messageTime());
        event.setType(dto.type());
        return event;
    }
    
}

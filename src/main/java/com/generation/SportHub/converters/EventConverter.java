package com.generation.SportHub.converters;

import java.util.List;

import org.springframework.stereotype.Service;

import com.generation.SportHub.dto.BuyerSummaryDTO;
import com.generation.SportHub.dto.EventAnswerSummaryDTO;
import com.generation.SportHub.dto.EventDTO;
import com.generation.SportHub.entity.Buyer;
import com.generation.SportHub.entity.Event;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EventConverter implements GenericConverter<EventDTO, Event> {

    private final BuyerSummaryConverter bsc;

    @Override
    public EventDTO fromEntityToDto(Event entity) {
        Buyer b= entity.getBuyer();
        BuyerSummaryDTO bsDto= new BuyerSummaryDTO(
            b.getId(),
            b.getUsername(),
            b.getName(),
            b.getSurname()

        );
        List<EventAnswerSummaryDTO> eventsAnswer =
            entity.getEventAnswers()
            .stream()
            .map(answer -> new EventAnswerSummaryDTO(
                answer.getId(),
                bsc.fromEntityToDto(answer.getBuyer()),
                answer.getText(),
                answer.getCreatedAt()
            ))
            .toList();

        return new EventDTO(
            entity.getId(),
            bsDto,
            entity.getEventTitle(),
            entity.getMessage(),
            entity.getEventDate(),
            entity.getMessageTime(),
            entity.getType(),
            eventsAnswer
        );
    }

    @Override
    public Event fromDtoToEntity(EventDTO dto) {
        Event event= new Event();
        event.setId(dto.id());
        event.setBuyer(bsc.fromDtoToEntity(dto.buyer()));
        event.setEventTitle(dto.eventTitle());
        event.setMessage(dto.message());
        event.setEventDate(dto.eventDate());
        event.setMessageTime(dto.messageTime());
        event.setType(dto.type());
        return event;
    }
    
}

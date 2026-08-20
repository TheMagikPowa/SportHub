package com.generation.SportHub.converters;

import org.springframework.stereotype.Service;

import com.generation.SportHub.dto.BuyerSummaryDTO;
import com.generation.SportHub.dto.EventAnswerDTO;
import com.generation.SportHub.dto.EventSummaryDTO;
import com.generation.SportHub.entity.Buyer;
import com.generation.SportHub.entity.Event;
import com.generation.SportHub.entity.EventAnswer;

import lombok.RequiredArgsConstructor;

/**
 * EventAnswerConverter
 */
@Service
@RequiredArgsConstructor
public class EventAnswerConverter implements GenericConverter<EventAnswerDTO, EventAnswer> {

    private final BuyerSummaryConverter buyerSummaryConverter;
    private final EventSummaryConverter eventSummaryConverter;

    @Override
    public EventAnswerDTO fromEntityToDto(EventAnswer entity) {
        Buyer b= entity.getBuyer();
        BuyerSummaryDTO bsDto= new BuyerSummaryDTO(
            b.getId(),
            b.getUsername(),
            b.getName(),
            b.getSurname()

        );
        Event e= new Event();
        EventSummaryDTO esDto= new EventSummaryDTO(
            e.getId(),
            e.getEventTitle(),
            e.getEventDate(),
            e.getType()
        );
        return new EventAnswerDTO(
            entity.getId(),
            bsDto,
            entity.getText(),
            entity.getCreatedAt(),
            esDto
        );
    }

    @Override
    public EventAnswer fromDtoToEntity(EventAnswerDTO dto) {
        EventAnswer eventAnswer = new EventAnswer();
        eventAnswer.setId(dto.id());
        eventAnswer.setBuyer(buyerSummaryConverter.fromDtoToEntity(dto.buyer()));
        eventAnswer.setText(dto.text());
        eventAnswer.setCreatedAt(dto.createdAt());
        eventAnswer.setEvent(eventSummaryConverter.fromDtoToEntity(dto.event()));
        return eventAnswer;
    }

}

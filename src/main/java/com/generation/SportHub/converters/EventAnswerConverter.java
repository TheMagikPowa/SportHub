package com.generation.SportHub.converters;

import com.generation.SportHub.dto.EventAnswerDTO;
import com.generation.SportHub.entity.EventAnswer;

import lombok.RequiredArgsConstructor;

/**
 * EventAnswerConverter
 */
@RequiredArgsConstructor
public class EventAnswerConverter implements GenericConverter<EventAnswerDTO, EventAnswer> {

    private final BuyerConverter buyerConverter;
    private final EventConverter eventConverter;

    @Override
    public EventAnswerDTO fromEntityToDto(EventAnswer entity) {
        return new EventAnswerDTO(
            entity.getId(),
            buyerConverter.fromEntityToDto(entity.getBuyer()),
            entity.getText(),
            entity.getCreatedAt(),
            eventConverter.fromEntityToDto(entity.getEvent())
        );
    }

    @Override
    public EventAnswer fromDtoToEntity(EventAnswerDTO dto) {
        EventAnswer eventAnswer = new EventAnswer();
        eventAnswer.setId(dto.id());
        eventAnswer.setBuyer(buyerConverter.fromDtoToEntity(dto.buyer()));
        eventAnswer.setText(dto.text());
        eventAnswer.setCreatedAt(dto.createdAt());
        eventAnswer.setEvent(eventConverter.fromDtoToEntity(dto.event()));
        return eventAnswer;
    }

}

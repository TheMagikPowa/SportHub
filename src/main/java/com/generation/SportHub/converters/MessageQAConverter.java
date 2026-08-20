package com.generation.SportHub.converters;

import org.springframework.stereotype.Service;

import com.generation.SportHub.dto.MessageQADTO;
import com.generation.SportHub.entity.MessageQA;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageQAConverter implements GenericConverter<MessageQADTO, MessageQA> {

    private final QuestionQAConverter qConverter;
    private final PersonConverter pConverter;
    @Override
    public MessageQADTO fromEntityToDto(MessageQA entity) {
        return new MessageQADTO(
            entity.getId(),
            qConverter.fromEntityToDto(entity.getQuestion()),
            pConverter.fromEntityToDto(entity.getStaff()),
            entity.getText(),
            entity.getCreatedAt()

        );
    }

    @Override
    public MessageQA fromDtoToEntity(MessageQADTO dto) {
        return new MessageQA(dto.id(), qConverter.fromDtoToEntity(dto.question()), 
        pConverter.fromDtoToEntity(dto.staff()), dto.text(), dto.createdAt());
        
    }
    
}

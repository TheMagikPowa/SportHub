package com.generation.SportHub.converters;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.generation.SportHub.dto.QuestionQADTO;
import com.generation.SportHub.entity.QuestionQA;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionQAConverter implements GenericConverter<QuestionQADTO, QuestionQA> {

    BuyerConverter buyerConverter;
    @Override
    public QuestionQADTO fromEntityToDto(QuestionQA entity) {
        return new QuestionQADTO(
            entity.getId(),
            buyerConverter.fromEntityToDto(entity.getBuyer()),
            entity.getTitle(),
            entity.getMessage(),
            entity.getStatus(),
            entity.getCreateTime()
        );
    }

    @Override
    public QuestionQA fromDtoToEntity(QuestionQADTO dto) {
        return new QuestionQA(
            dto.id(),
            buyerConverter.fromDtoToEntity(dto.buyer()),
            dto.title(),
            dto.message(),
            dto.status(),
            dto.createTime(),
            new ArrayList<>()
        );
    }
    
}

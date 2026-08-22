package com.generation.SportHub.service;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.generation.SportHub.converters.EventAnswerConverter;
import com.generation.SportHub.dto.EventAnswerDTO;
import com.generation.SportHub.entity.EventAnswer;
import com.generation.SportHub.repository.EventAnswerRepository;

@Service
public class EventAnswerService extends GenericService<Long, EventAnswer, EventAnswerDTO, EventAnswerConverter, EventAnswerRepository> {
    public EventAnswerService(EventAnswerRepository ear, EventAnswerConverter eac, ApplicationContext ac){
        super(ear, eac, ac);
    }

    @Override
    public EventAnswer construct(Map<String, String> params) {
        EventAnswer ea= getContext().getBean(EventAnswer.class, params);
        return ea;
    }
}

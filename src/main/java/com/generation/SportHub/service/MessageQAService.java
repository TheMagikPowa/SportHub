package com.generation.SportHub.service;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.generation.SportHub.converters.MessageQAConverter;
import com.generation.SportHub.dto.MessageQADTO;
import com.generation.SportHub.entity.MessageQA;
import com.generation.SportHub.repository.MessageQARepository;

@Service
public class MessageQAService extends GenericService<Long, MessageQA, MessageQADTO, MessageQAConverter, MessageQARepository>{

    public MessageQAService(MessageQARepository mr, MessageQAConverter mc, ApplicationContext ac) {
        super(mr, mc, ac);
    }

    @Override
    public MessageQA construct(Map<String, String> params) {
        MessageQA m= getContext().getBean(MessageQA.class, params);
        return m;
    }
    
}

package com.generation.SportHub.service;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.generation.SportHub.converters.QuestionQAConverter;
import com.generation.SportHub.dto.QuestionQADTO;
import com.generation.SportHub.entity.QuestionQA;
import com.generation.SportHub.repository.QuestionQARepository;

public class QuestionQAService extends GenericService<Long, QuestionQA, QuestionQADTO, QuestionQAConverter, QuestionQARepository>{
    
    public QuestionQAService(QuestionQARepository qr, QuestionQAConverter qc, ApplicationContext ac) {
        super(qr, qc, ac);
    }

    @Override
    public QuestionQA construct(Map<String, String> params) {
        QuestionQA q= getContext().getBean(QuestionQA.class, params);
        return q;
    }

    
}

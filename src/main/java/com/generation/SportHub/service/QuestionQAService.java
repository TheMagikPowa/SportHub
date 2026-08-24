package com.generation.SportHub.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.generation.SportHub.converters.QuestionQAConverter;
import com.generation.SportHub.dto.QuestionQADTO;
import com.generation.SportHub.entity.Buyer;
import com.generation.SportHub.entity.MessageQA;
import com.generation.SportHub.entity.Person;
import com.generation.SportHub.entity.QuestionQA;
import com.generation.SportHub.entity.enums.QuestionStatus;
import com.generation.SportHub.repository.MessageQARepository;
import com.generation.SportHub.repository.QuestionQARepository;

import lombok.RequiredArgsConstructor;

@Service
public class QuestionQAService extends GenericService<Long, QuestionQA, QuestionQADTO, QuestionQAConverter, QuestionQARepository>{
    
    private final QuestionQARepository qQARepo;
    private final MessageQARepository mQARepo; 

    public QuestionQAService(QuestionQARepository qr, QuestionQAConverter qc, ApplicationContext ac, MessageQARepository mQARepo) {
        super(qr, qc, ac);
        this.qQARepo= qr;
        this.mQARepo = mQARepo;
    }

    @Override
    public QuestionQA construct(Map<String, String> params) {
        QuestionQA q= getContext().getBean(QuestionQA.class, params);
        return q;
    }

    public List<QuestionQA> findAllQuestions() {
        return qQARepo.findAll();
    }

    public Optional<QuestionQA> findQuestionById(Long id) {
        return qQARepo.findById(id);
    }

   
   public QuestionQA saveQuestion(QuestionQA questionQA, Buyer buyer) {
       
        questionQA.setBuyer(buyer);
        questionQA.setStatus(QuestionStatus.OPEN); //stato default
        questionQA.setCreateTime(Instant.now());  

        return qQARepo.save(questionQA);
    }

    
    public MessageQA addAnswerToQuestion(Long questionId, String text, Person staffPerson) {
        QuestionQA question = qQARepo.findById(questionId).orElseThrow(() -> new RuntimeException("Question id " + questionId +"impossible to find"));

        MessageQA message = new MessageQA();
        message.setQuestion(question);
        message.setText(text);
        message.setStaff(staffPerson);
        message.setCreatedAt(Instant.now());

        return mQARepo.save(message);
    }
}

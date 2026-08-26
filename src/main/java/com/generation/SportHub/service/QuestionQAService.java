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
import com.generation.SportHub.repository.BuyerRepository;
import com.generation.SportHub.repository.MessageQARepository;
import com.generation.SportHub.repository.QuestionQARepository;
import com.generation.SportHub.repository.PersonRepository;

@Service
public class QuestionQAService extends GenericService<Long, QuestionQA, QuestionQADTO, QuestionQAConverter, QuestionQARepository>{
    
    private final QuestionQARepository qQARepo;
    private final MessageQARepository mQARepo; 
    private final BuyerRepository buyerRepository;
    private final PersonRepository personRepository;

    public QuestionQAService(
                QuestionQARepository qr,
                QuestionQAConverter qc,
                ApplicationContext ac,
                MessageQARepository mQARepo,
                BuyerRepository buyerRepository,
                PersonRepository personRepository) {

            super(qr, qc, ac);
            this.qQARepo = qr;
            this.mQARepo = mQARepo;
            this.buyerRepository = buyerRepository;
            this.personRepository = personRepository;
        }

    @Override
    public QuestionQA construct(Map<String, String> params) {
        QuestionQA q= getContext().getBean(QuestionQA.class, params);
        return q;
    }

    public List<QuestionQA> findAllQuestions() {
        return qQARepo.findAllByOrderByCreateTimeDesc();
    }

    public Optional<QuestionQA> findQuestionById(Long id) {
        return qQARepo.findById(id);
    }

   
    public QuestionQA saveQuestion(
            QuestionQA questionQA,
            String email) {

        Buyer buyer = buyerRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                    new RuntimeException("Buyer not found"));

        questionQA.setBuyer(buyer);
        questionQA.setStatus(QuestionStatus.OPEN);
        questionQA.setCreateTime(Instant.now());

        return qQARepo.save(questionQA);
    }

    
    public MessageQA addAnswerToQuestion(
            Long questionId,
            String text,
            String email) {

        QuestionQA question = qQARepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException(
                        "Question not found"));

        Person staffPerson = personRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException(
                        "Staff user not found"));

        MessageQA message = new MessageQA();
        message.setQuestion(question);
        message.setText(text);
        message.setStaff(staffPerson);
        message.setCreatedAt(Instant.now());

        return mQARepo.save(message);
    }
}

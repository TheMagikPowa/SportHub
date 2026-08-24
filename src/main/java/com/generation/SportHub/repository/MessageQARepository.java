package com.generation.SportHub.repository;

import com.generation.SportHub.entity.MessageQA;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageQARepository extends JpaRepository<MessageQA, Long> {
    List<MessageQA> findByQuestionId(Long questionId);
}

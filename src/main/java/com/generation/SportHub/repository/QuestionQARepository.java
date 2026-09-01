package com.generation.SportHub.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.SportHub.entity.QuestionQA;

public interface QuestionQARepository extends JpaRepository<QuestionQA, Long> {
    List<QuestionQA> findAllByOrderByCreateTimeDesc();
}

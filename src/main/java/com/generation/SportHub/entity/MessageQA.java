package com.generation.SportHub.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "messages_qa")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class MessageQA extends GenericEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionQA question;

    @ManyToOne //da controllare 
    @JoinColumn(name = "staff_people_user_id")
    private Person staff;

    @Column(nullable = false, length = 1000)
    private String text;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

  
}

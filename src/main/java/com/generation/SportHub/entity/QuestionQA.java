package com.generation.SportHub.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.generation.SportHub.entity.enums.QuestionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "questions_qa")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class QuestionQA extends GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(
    name = "buyers_people_user_id",
    columnDefinition = "BIGINT UNSIGNED"
)
    private Buyer buyer;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length =100)
    private String message; 

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private QuestionStatus status; 

    @Column(name = "created_at", updatable = false)
    private Instant createTime;

    @OneToMany(mappedBy = "question")
    private List<MessageQA> answers = new ArrayList<>();
    
}

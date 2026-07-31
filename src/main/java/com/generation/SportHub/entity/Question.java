package com.generation.SportHub.entity;

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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "questions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyers_people_user_id")
    private Buyer buyer;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length =100)
    private String message; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionStatus status; 
    
    /* CREATE TABLE questions (
	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    buyers_people_user_id BIGINT UNSIGNED,
    title VARCHAR (50) NOT NULL,
    message VARCHAR (1000) NOT NULL,
    state ENUM("OPEN", "CLOSED") NOT NULL,
	FOREIGN KEY (buyers_people_user_id) REFERENCES buyers(user_people_id)
		ON UPDATE CASCADE
        ON DELETE SET NULL
);
 */
}

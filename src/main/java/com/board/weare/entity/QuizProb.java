package com.board.weare.entity;

import javax.persistence.*;

@Entity
@Table(name="tb_quiz")
public class QuizProb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;

    @Column(length = 4000,nullable = false)
    private String name;

}

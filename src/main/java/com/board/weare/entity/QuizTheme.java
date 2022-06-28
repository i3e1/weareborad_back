package com.board.weare.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "tb_quiz_theme")
@Getter
@ToString
@NoArgsConstructor
public class QuizTheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title; // 테마 이름

    private String explain; // 테마 설명

    private Integer time; // 테마 제한시간

    private String category; // 테마 카테고리

    private Double star; // 난이도

    private Double activity; // 활동성

    private Integer recommendedPeople; // 추천인원

    private 

    private String imgUrl; // 테마 이미지 경로

}

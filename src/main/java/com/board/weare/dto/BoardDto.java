package com.board.weare.dto;

import com.board.weare.entity.Account;
import com.board.weare.entity.Board;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;

public class BoardDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Info {
        private Long boardId;
        private String title;
        private String content;
        private String category;
        private String etc;

        public Info(Board board) {
            this.boardId = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.category = board.getCategory();
            this.etc = board.getEtc();
        }
    }


    @Data
    @NoArgsConstructor
    public static class Request {
        private Long boardId;
        @Max(value = 30, message = "제목은 최대 30자 입니다.")
        private String title;
        @Min(0)@Max(value = 3000,message = "내용은 최대 3000자 입니다.")
        private String content;
        private String category;
        private String etc;
    }


}

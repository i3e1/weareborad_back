package com.board.weare.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BasicException extends RuntimeException{
    private Integer status;
    private String code;
    private String message;
}

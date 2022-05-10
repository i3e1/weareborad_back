package com.board.weare.exception.entities.jwt;


import com.board.weare.exception.BasicException;

public class TokenIsNotGiven extends BasicException {
    public TokenIsNotGiven(String name) {
        super(400, "token_is_not_null", name+"토큰이 넘어오지 않았습니다.");
    }
}
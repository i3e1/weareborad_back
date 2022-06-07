package com.board.weare.exception;


import com.board.weare.service.AccountServiceImpl;

public class DuplicateIdException extends BasicException {

    public DuplicateIdException(){
        this("data");
    }

    public DuplicateIdException(String entity){
        this(entity, "데이터가 중복됩니다.");
    }

    public DuplicateIdException(String entity, String message){
        super(409, entity + "_conflict", message);
    }
}
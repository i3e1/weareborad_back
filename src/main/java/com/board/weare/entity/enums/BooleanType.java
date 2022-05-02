package com.board.weare.entity.enums;

import com.jsol.mcall.operator.IntegerOp;

public class BooleanType {

    public static boolean rand(){
        if(IntegerOp.getRandInt(2) == 1){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
}

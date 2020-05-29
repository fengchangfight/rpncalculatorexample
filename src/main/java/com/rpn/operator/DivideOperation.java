package com.rpn.operator;

import java.math.BigDecimal;

/**
 * @author xiefengchang
 */
public class DivideOperation extends BaseBinaryOperation {
    public DivideOperation(){
        super("/");
    }
    @Override
    public BigDecimal doOperation(BigDecimal op1, BigDecimal op2){
        if(op2.compareTo(BigDecimal.ZERO)==0){
            System.out.println("不能除以零");
            throw new RuntimeException("不能除以零");
        }
        return op1.divide(op2,15, BigDecimal.ROUND_DOWN);
    }
}

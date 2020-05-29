package com.rpn.operator;

import java.math.BigDecimal;

public class MinusOperation extends BaseBinaryOperation {
    public MinusOperation(){
        super("-");
    }
    @Override
    public BigDecimal doOperation(BigDecimal op1, BigDecimal op2){
        return op1.subtract(op2);
    }
}

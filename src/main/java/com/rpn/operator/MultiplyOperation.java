package com.rpn.operator;

import java.math.BigDecimal;

public class MultiplyOperation extends BaseBinaryOperation {
    public MultiplyOperation(){
        super("*");
    }
    @Override
    public BigDecimal doOperation(BigDecimal op1, BigDecimal op2){
        return op1.multiply(op2);
    }
}

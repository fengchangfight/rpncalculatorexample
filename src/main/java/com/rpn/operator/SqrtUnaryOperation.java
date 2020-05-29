package com.rpn.operator;

import java.math.BigDecimal;

public class SqrtUnaryOperation extends BaseUnaryOperation {
    public SqrtUnaryOperation(){
        super("sqrt");
    }

    @Override
    public BigDecimal doOperation(BigDecimal op){
        return new BigDecimal(Math.sqrt(op.doubleValue()));
    }
}
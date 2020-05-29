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
        return op1.divide(op2,15, BigDecimal.ROUND_DOWN);
    }
}

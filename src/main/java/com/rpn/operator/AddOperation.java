package com.rpn.operator;

import java.math.BigDecimal;

/**
 * @author xiefengchang
 */
public class AddOperation extends BaseBinaryOperation {
    public AddOperation(){
        super("+");
    }
    @Override
    public BigDecimal doOperation(BigDecimal op1, BigDecimal op2){
        return op1.add(op2);
    }
}

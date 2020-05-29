package com.rpn.operator;

import java.math.BigDecimal;

/**
 * @author xiefengchang
 */
public class SqrtUnaryOperation extends BaseUnaryOperation {
    public SqrtUnaryOperation(){
        super("sqrt");
    }

    @Override
    public BigDecimal doOperation(BigDecimal op){
        return sqrt(op);
    }

    private static BigDecimal sqrt(BigDecimal value) {
        BigDecimal x = new BigDecimal(Math.sqrt(value.doubleValue()));
        return x.add(new BigDecimal(value.subtract(x.multiply(x)).doubleValue() / (x.doubleValue() * 2.0)));

    }
}

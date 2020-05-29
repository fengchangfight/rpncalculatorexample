package com.rpn.operator;

import java.math.BigDecimal;

/**
 * @author xiefengchang
 * 本类为一元运算符的抽象类，对应着某种具体的一元运算，例如sqrt
 */
public abstract class BaseUnaryOperation {
    private String operator;

    public BaseUnaryOperation(String o){
        this.operator = o;
    }

    public abstract BigDecimal doOperation(BigDecimal op);
}

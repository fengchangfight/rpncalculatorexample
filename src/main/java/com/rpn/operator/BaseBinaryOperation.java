package com.rpn.operator;

import java.math.BigDecimal;

/**
 * @author xiefengchang
 * 本类为二元运算符的抽象类，对应着某种具体的二元运算
 */
public abstract class BaseBinaryOperation {
    private String operator;

    public BaseBinaryOperation(String o){
        this.operator = o;
    }

    public abstract BigDecimal doOperation(BigDecimal op1, BigDecimal op2);
}

package com.rpn.operator;

public class OperatorFactory {
    public static BaseUnaryOperation makeUnaryOperation(String sign){
        switch (sign){
            case "sqrt":
                return new SqrtUnaryOperation();
                default:
                    throw new RuntimeException("illegal unary op sign detected");
        }
    }

    public static BaseBinaryOperation makeBinaryOperation(String sign){
        switch (sign){
            case "+":
                return new AddOperation();
            case "-":
                return new MinusOperation();
            case "*":
                return new MultiplyOperation();
            case "/":
                return new DivideOperation();
                default:
                    throw new RuntimeException("illegal binary op sign detected");

        }
    }
}

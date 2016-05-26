package com.tuzhida.tools;

import java.math.BigDecimal;

public enum Counts {
    ADD, MINUS, MULTIPLY, DIVIDE;
    public String Values(String num1, String num2) {
        BigDecimal number1 = new BigDecimal(num1);
        BigDecimal number2 = new BigDecimal(num2);
        BigDecimal number = BigDecimal.valueOf(0);
        switch (this) {
            case ADD:
                number = number1.add(number2);
                break;
            case MINUS:
                number = number1.subtract(number2);
                break;
            case MULTIPLY:
                number = number1.multiply(number2);
                break;
            case DIVIDE:
                number = number1.divide(number2,2,BigDecimal.ROUND_HALF_UP);
                break;

        }
        return number.toString();

    }

}

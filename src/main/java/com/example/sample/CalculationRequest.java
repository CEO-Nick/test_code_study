package com.example.sample;

public class CalculationRequest {

    private final long num1;
    private final long num2;
    private final String operator;

    public CalculationRequest(String[] parts) {
        if (parts.length != 3) {
            throw new BadRequestException(
                "Invalid input format. Expected format: num1 operator num2");
        }

        if (parts[1].length() != 1) {
            throw new InvalidOperatorException();
        }

        if (isInvalidOperator(parts[1])) {
            throw new InvalidOperatorException();
        }

        num1 = Long.parseLong(parts[0]);
        num2 = Long.parseLong(parts[2]);
        operator = parts[1];
    }

    private boolean isInvalidOperator(String operator) {
        return !operator.equals("+") &&
            !operator.equals("-") &&
            !operator.equals("*") &&
            !operator.equals("/");
    }

    public long getNum1() {
        return num1;
    }

    public long getNum2() {
        return num2;
    }

    public String getOperator() {
        return operator;
    }

}

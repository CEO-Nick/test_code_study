package com.example.sample;

import java.util.Scanner;

public class SampleApplication {

    public static void main(String[] args) {
        CalculationRequest calculationRequest = new CalculationRequestReader().read();
        long answer = new Calculator().calculate(
            calculationRequest.getNum1(),
            calculationRequest.getNum2(),
            calculationRequest.getOperator()
        );

        System.out.println("The result is: " + answer);
    }

}

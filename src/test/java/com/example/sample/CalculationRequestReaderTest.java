package com.example.sample;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import org.junit.Test;

public class CalculationRequestReaderTest {

    @Test
    public void System_in으로_데이터를_읽어들일_수_있다() {
        // given
        CalculationRequestReader calculationRequestReader = new CalculationRequestReader();

        // when
        System.setIn(new ByteArrayInputStream("2 + 3".getBytes()));
        CalculationRequest result = calculationRequestReader.read();

        // then
        assertEquals(2, result.getNum1());
        assertEquals(3, result.getNum2());
        assertEquals("+", result.getOperator());
    }

}
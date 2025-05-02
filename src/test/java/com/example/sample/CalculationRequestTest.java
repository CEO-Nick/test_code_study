package com.example.sample;

import static org.junit.Assert.*;

import org.junit.Test;

public class CalculationRequestTest {

    @Test
    public void 유효한_숫자를_파싱할_수_있다() {
        // given
        String[] parts = new String[] {"2", "+", "3"};

        // when
        CalculationRequest calculationRequest = new CalculationRequest(parts);

        // then
        assertEquals(2, calculationRequest.getNum1());
        assertEquals(3, calculationRequest.getNum2());
        assertEquals("+", calculationRequest.getOperator());
    }

    @Test
    public void 세자리_숫자가_넘어가는_유효한_숫자를_파싱할_수_있다() {
        // given
        String[] parts = new String[] {"122", "+", "1223"};

        // when
        CalculationRequest calculationRequest = new CalculationRequest(parts);

        // then
        assertEquals(122, calculationRequest.getNum1());
        assertEquals(1223, calculationRequest.getNum2());
        assertEquals("+", calculationRequest.getOperator());
    }

    @Test
    public void 유효한_길이의_숫자가_들어오지_않으면_에러를_던진다() {
        // given
        String[] parts = new String[] {"2", "+", "3", "4"};

        // when
        assertThrows(BadRequestException.class, () -> {
            new CalculationRequest(parts);
        });
    }

    @Test
    public void 유효하지_않은_연산자가_들어오면_에러를_던진다() {
        // given
        String[] parts = new String[] {"2", "x", "3"};

        // when
        assertThrows(InvalidOperatorException.class, () -> {
            new CalculationRequest(parts);
        });
    }

    @Test
    public void 유효하지_않은_길이의_연산자가_들어오면_예외를_던진다() {
        // given
        String[] parts = new String[] {"2", "+-", "3"};

        // when
        assertThrows(InvalidOperatorException.class, () -> {
            new CalculationRequest(parts);
        });
    }

}
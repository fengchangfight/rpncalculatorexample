package com.rpn;
/**
 * @author xiefengchang
 */

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CalculatorTest {

    public static final List<String> inputs = Arrays.asList(
            "5 2",
            "2 sqrt clear 9 sqrt",
            "5 2 - 3 - clear",
            "5 4 3 2 undo undo * 5 * undo",
            "7 12 2 / * 4 /",
            "1 2 3 4 5 * clear 3 4 -",
            "1 2 3 4 5 * * * *",
            "1 2 3 * 5 + * * 6 5",
            "abc 3 d",
            "1 2 kk",
            "2 0 /");

    public static final List<String> stackState = Arrays.asList(
            "stack: 5 2",
            "stack: 3",
            "stack:",
            "stack: 20 5",
            "stack: 10.5",
            "stack: -1",
            "stack: 120",
            "stack: 11",
            "stack:",
            "stack: 1 2",
            "stack: 2 0");

    public static final List<String> messages = Arrays.asList(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "operator * (position: 15): insucient parameters",
            "非法输入...",
            "非法输入...",
            "不能除以零");

    @Test
    public void testCalculator() {
        Calculator rpn = new Calculator();
        rpn.initMyState();

        for(int i=0;i<inputs.size();i++){
            rpn.reset();
            String input = inputs.get(i);
            String expectedOutput = stackState.get(i);
            String expectedMessage = messages.get(i);
            rpn.getStateManager().addNewLine(input);
            rpn.getStateManager().parseLine();
            // check state
            String actualContent = rpn.getStateManager().getStackContent();
            String actualMessage = rpn.getStateManager().getMessage();
            assertEquals(expectedOutput, actualContent);
            assertEquals(expectedMessage, actualMessage);
        }
    }
}

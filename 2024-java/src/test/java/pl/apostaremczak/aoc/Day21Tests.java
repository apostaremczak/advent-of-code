package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day21Tests {
    private final Day21 day21 = new Day21("src/test/resources/21.txt");

    @Test
    public void testSolvePart1() {
        Long result = day21.solvePart1();
        assertEquals(126384L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day21.solvePart2();
        assertEquals(154115708116294L, result);
    }

    @Test
    public void testMoveToVisualRepresentations() {
        Keypad numericKeypad = Keypad.buildNumeric();
        Set<String> possibleMovesWithoutDirectionChange = Set.of(">^^A", "^^>A");
        Set<String> result = numericKeypad.allMovesBetweenButtons('2', '9');
        assertEquals(possibleMovesWithoutDirectionChange, result);
    }
}

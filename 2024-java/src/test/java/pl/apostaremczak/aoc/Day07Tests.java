package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Day07Tests {
    private final Day07 day07 = new Day07("src/test/resources/07.txt");

    @Test
    public void testSolvePart1() {
        Long result = day07.solvePart1();
        assertEquals(3749L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day07.solvePart2();
        assertEquals(11387L, result);
    }

    @Test
    public void testEquationDfsCanPlaceOperators() {
        assertTrue(new Equation("292: 11 6 16 20").canPlaceOperators());
        assertFalse(new Equation("192: 17 8 14").canPlaceOperators());
        assertTrue(new Equation("15: 3 5 1 1 1 1 1 1 1 1 1 1 1 1").canPlaceOperators());
        assertTrue(new Equation("97902: 194 3 8 2 3 5 7 7").canPlaceOperators());
    }

    @Test
    public void testEquationDfsCanPlaceOperatorsWithConcat() {
        assertTrue(new Equation("156: 15 6").canPlaceOperatorsWithConcat());
        assertTrue(new Equation("7290: 6 8 6 15").canPlaceOperatorsWithConcat());
        assertTrue(new Equation("192: 17 8 14").canPlaceOperatorsWithConcat());
    }
}

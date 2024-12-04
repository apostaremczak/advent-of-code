package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day04Tests {
    private final Day04 day04 = new Day04("src/test/resources/04.txt");

    @Test
    public void testSolvePart1() {
        Long result = day04.solvePart1();
        assertEquals(18L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day04.solvePart2();
        assertEquals(9L, result);
    }
}

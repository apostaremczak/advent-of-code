package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day06Tests {
    private final Day06 day06 = new Day06("src/test/resources/06.txt");

    @Test
    public void testSolvePart1() {
        Long result = day06.solvePart1();
        assertEquals(0L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day06.solvePart2();
        assertEquals(0L, result);
    }
}

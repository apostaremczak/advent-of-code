package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day24Tests {
    private final Day24 day24 = new Day24("src/test/resources/24.txt");

    @Test
    public void testSolvePart1Small() {
        Day24 day = new Day24("src/test/resources/24_small.txt");
        Long result = day.solvePart1();
        assertEquals(4L, result);
    }

    @Test
    public void testSolvePart1() {
        Long result = day24.solvePart1();
        assertEquals(2024L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day24.solvePart2();
        assertEquals(0L, result);
    }
}

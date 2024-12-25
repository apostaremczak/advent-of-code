package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day25Tests {
    private final Day25 day25 = new Day25("src/test/resources/25.txt");

    @Test
    public void testSolvePart1() {
        Long result = day25.solvePart1();
        assertEquals(3L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day25.solvePart2();
        assertEquals(0L, result);
    }
}

package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day20Tests {
    private final Day20 day20 = new Day20("src/test/resources/20.txt", 1L);

    @Test
    public void testSolvePart1() {
        Long result = day20.solvePart1();
        assertEquals(44L, result);
    }

    @Test
    public void testSolvePart2() {
        final Day20 day = new Day20("src/test/resources/20.txt", 50L);

        Long result = day.solvePart2();
        assertEquals(285L, result);
    }
}

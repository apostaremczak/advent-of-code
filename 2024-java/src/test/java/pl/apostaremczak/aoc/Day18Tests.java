package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day18Tests {
    private final Day18 day18 = new Day18("src/test/resources/18.txt", 6, 6, 12);

    @Test
    public void testSolvePart1() {
        Long result = day18.solvePart1();
        assertEquals(22L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day18.solvePart2();
        assertEquals(0L, result);
    }
}

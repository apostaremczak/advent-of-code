package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day23Tests {
    private final Day23 day23 = new Day23("src/test/resources/23.txt");

    @Test
    public void testSolvePart1() {
        Long result = day23.solvePart1();
        assertEquals(7L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day23.solvePart2();
        assertEquals(0L, result);
    }
}

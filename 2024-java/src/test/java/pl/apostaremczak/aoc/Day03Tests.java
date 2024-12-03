package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day03Tests {
    private final Day03 day03 = new Day03("src/test/resources/03.txt");

    @Test
    public void testSolvePart1() {
        Long result = day03.solvePart1();
        assertEquals(161L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day03.solvePart2();
        assertEquals(48L, result);
    }
}

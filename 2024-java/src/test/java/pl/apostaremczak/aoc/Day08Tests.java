package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day08Tests {
    private final Day08 day08 = new Day08("src/test/resources/08.txt");

    @Test
    public void testSolvePart1() {
        Long result = day08.solvePart1();
        assertEquals(14L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day08.solvePart2();
        assertEquals(34L, result);
    }

    @Test
    public void testSolvePart2Additional() {
        Day08 day = new Day08("src/test/resources/08_2.txt");
        Long result = day.solvePart2();
        assertEquals(9L, result);
    }
}

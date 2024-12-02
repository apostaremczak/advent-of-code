package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day01Tests {
    private final Day01 day01 = new Day01("src/test/resources/01.txt");

    @Test
    public void testSolvePart1() {
        Long result = day01.solvePart1();
        assertEquals(11L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day01.solvePart2();
        assertEquals(31L, result);
    }
}

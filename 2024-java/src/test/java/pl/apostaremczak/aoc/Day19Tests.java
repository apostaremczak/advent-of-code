package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Day19Tests {
    private final Day19 day19 = new Day19("src/test/resources/19.txt");

    @Test
    public void testCountPossibleArrangements() {
        assertEquals(4, day19.countPossibleArrangements("gbbr"));
        assertEquals(6, day19.countPossibleArrangements("rrbgbr"));
        assertEquals(1, day19.countPossibleArrangements("bwurrg"));
    }

    @Test
    public void testSolvePart1() {
        Long result = day19.solvePart1();
        assertEquals(6L, result);
    }

    @Test
    public void testSolvePart2() {
        day19.solvePart1();
        Long result = day19.solvePart2();
        assertEquals(16L, result);
    }
}

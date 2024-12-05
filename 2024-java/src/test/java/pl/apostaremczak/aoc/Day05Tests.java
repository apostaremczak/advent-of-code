package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day05Tests {
    private final Day05 day05 = new Day05("src/test/resources/05.txt");

    @Test
    public void testSolvePart1() {
        Long result = day05.solvePart1();
        assertEquals(143L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day05.solvePart2();
        assertEquals(123L, result);
    }

    @Test
    public void testGetMiddleValue() {
        assertEquals(61, Day05.getMiddleValue(Arrays.asList("75,47,61,53,29".split(","))));
        assertEquals(29, Day05.getMiddleValue(Arrays.asList("75,29,13".split(","))));
    }
}

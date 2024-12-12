package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day12Tests {
    private final Day12 day12 = new Day12("src/test/resources/12.txt");

    @Test
    public void testSolvePart1() {
        Long result = day12.solvePart1();
        assertEquals(1930L, result);
    }

    @Test
    public void testTotalPriceABCDE() {
        Day12 day = new Day12("src/test/resources/12_ABCDE.txt");
        Long result = day.solvePart1();
        assertEquals(140L, result);
    }

    @Test
    public void testTotalPriceXO() {
        Day12 day = new Day12("src/test/resources/12_XO.txt");
        Long result = day.solvePart1();
        assertEquals(772L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day12.solvePart2();
        assertEquals(1206L, result);
    }
}

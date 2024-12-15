package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day15Tests {
    private final Day15 day15 = new Day15("src/test/resources/15.txt");

    @Test
    public void testSolvePart1Small() {
        Day15 day = new Day15("src/test/resources/15_small.txt");
        Long result = day.solvePart1();
        assertEquals(2028L, result);
    }

    @Test
    public void testSolvePart2Small() {
        Day15 day = new Day15("src/test/resources/15_small_2.txt");
        Long result = day.solvePart2();
        assertEquals(618L, result);
    }

    @Test
    public void testSolvePart1() {
        Long result = day15.solvePart1();
        assertEquals(10092L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day15.solvePart2();
        assertEquals(9021L, result);
    }
}

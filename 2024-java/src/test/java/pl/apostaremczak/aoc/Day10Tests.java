package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;
import pl.apostaremczak.aoc.util.Coord2D;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day10Tests {
    private final Day10 day10 = new Day10("src/test/resources/10.txt");

    @Test
    public void testSolvePart1() {
        Long result = day10.solvePart1();
        assertEquals(36L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day10.solvePart2();
        assertEquals(81L, result);
    }

    @Test
    public void testGetTrailheadScore() {
        Coord2D start1 = new Coord2D(0, 2);
        Integer result1 = day10.getTrailheadScore(start1);
        assertEquals(5, result1);

        Coord2D start2 = new Coord2D(0, 4);
        Integer result2 = day10.getTrailheadScore(start2);
        assertEquals(6, result2);

        Coord2D start3 = new Coord2D(2, 4);
        Integer result3 = day10.getTrailheadScore(start3);
        assertEquals(5, result3);

        Coord2D start4 = new Coord2D(4, 6);
        Integer result4 = day10.getTrailheadScore(start4);
        assertEquals(3, result4);

        Coord2D start5 = new Coord2D(5, 2);
        Integer result5 = day10.getTrailheadScore(start5);
        assertEquals(1, result5);

        Coord2D start6 = new Coord2D(5, 5);
        Integer result6 = day10.getTrailheadScore(start6);
        assertEquals(3, result6);

        Coord2D start7 = new Coord2D(6, 0);
        Integer result7 = day10.getTrailheadScore(start7);
        assertEquals(5, result7);

        Coord2D start8 = new Coord2D(6,6);
        Integer result8 = day10.getTrailheadScore(start8);
        assertEquals(3, result8);
    }
}

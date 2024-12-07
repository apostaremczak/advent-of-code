package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;
import pl.apostaremczak.aoc.util.Coord2D;
import pl.apostaremczak.aoc.util.DirectionSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day06Tests implements DirectionSupport {
    @Test
    public void testSolvePart1() {
        Day06 day06 = new Day06("src/test/resources/06.txt");
        Long result = day06.solvePart1();
        assertEquals(41L, result);
    }

    @Test
    public void testWillTrapGuard() {
        Day06 day06 = new Day06("src/test/resources/06.txt");
        Coord2D guardPosition = new Coord2D(6, 4);
        Coord2D guardDirection = TOP;
        Coord2D obstaclePosition = new Coord2D(6, 3);
        boolean result = day06.willTrapGuard(guardPosition, guardDirection, obstaclePosition);
        assertTrue(result);
    }

    @Test
    public void testWillTrapGuard2() {
        Day06 day06 = new Day06("src/test/resources/06.txt");
        Coord2D guardPosition = new Coord2D(8, 4);
        Coord2D guardDirection = TOP;
        Coord2D obstaclePosition = new Coord2D(8, 3);
        boolean result = day06.willTrapGuard(guardPosition, guardDirection, obstaclePosition);
        assertTrue(result);
    }

    @Test
    public void testSolvePart2() {
        Day06 day06 = new Day06("src/test/resources/06.txt");
        Long result = day06.solvePart2();
        assertEquals(6L, result);
    }

    @Test
    public void testSolvePart2AdditionalInput() {
        Day06 day6A = new Day06("src/test/resources/06_2.txt");
        Long result = day6A.solvePart2();
        assertEquals(1L, result);
    }

    @Test
    public void testSolvePart3AdditionalInput() {
        Day06 day6A = new Day06("src/test/resources/06_3.txt");
        Long result = day6A.solvePart2();
        assertEquals(1L, result);
    }

    @Test
    public void testSolvePart4AdditionalInput() {
        Day06 day6A = new Day06("src/test/resources/06_4.txt");
        Long result = day6A.solvePart2();
        assertEquals(1L, result);
    }

    @Test
    public void testSolvePart5AdditionalInput() {
        Day06 day6A = new Day06("src/test/resources/06_5.txt");
        Long result = day6A.solvePart2();
        assertEquals(0L, result);
    }
}

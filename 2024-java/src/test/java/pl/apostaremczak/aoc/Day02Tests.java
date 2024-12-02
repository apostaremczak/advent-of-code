package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day02Tests {
    private final Day02 day02 = new Day02("src/test/resources/02.txt");

    @Test
    public void testSolvePart1() {
        Long result = day02.solvePart1();
        assertEquals(2L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day02.solvePart2();
        assertEquals(4L, result);
    }
}

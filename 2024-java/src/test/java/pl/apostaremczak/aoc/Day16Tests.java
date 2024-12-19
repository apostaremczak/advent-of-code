package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day16Tests {
    private final Day16 day16 = new Day16("src/test/resources/16.txt");

    @Test
    public void testSolvePart1() {
        Long result = day16.solvePart1();
        assertEquals(7036L, result);
    }

    @Test
    public void testSolvePart1SecondExample() {
        Day16 day = new Day16("src/test/resources/16_2.txt");
        Long result = day.solvePart1();
        assertEquals(11048L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day16.solvePart2();
        assertEquals(45L, result);
    }

    @Test
    public void testSolvePart2SecondExample() {
        Day16 day = new Day16("src/test/resources/16_2.txt");
        Long result = day.solvePart2();
        assertEquals(64L, result);
    }
}

package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day11Tests {
    private final Day11 day11 = new Day11("src/test/resources/11.txt");

    @Test
    public void testSolvePart1() {
        Long result = day11.solvePart1();
        assertEquals(55312L, result);
    }

    @Test
    public void testStone() {
        List<Stone> result1 = new Stone(0L).blink();
        assertEquals(1, result1.getFirst().engravedNote());

        List<Stone> result2 = new Stone(1L).blink();
        assertEquals(2024, result2.getFirst().engravedNote());

        List<Stone> result3 = new Stone(10L).blink();
        assertEquals(List.of(new Stone(1L), new Stone(0L)), result3);
    }
}

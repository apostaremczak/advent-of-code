package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day22Tests {
    private final Day22 day22 = new Day22("src/test/resources/22.txt");

    @Test
    public void testSolvePart1() {
        Long result = day22.solvePart1();
        assertEquals(37327623L, result);
    }

    @Test
    public void testSolvePart2() {
        Day22 day = new Day22("src/test/resources/22_2.txt");
        Long result = day.solvePart2();
        assertEquals(23L, result);
    }

    @Test
    public void testEvolveSecret() {
        assertEquals(15887950L, MonkeySecretHelper.evolveSecret(123L));
        assertEquals(5908254L, MonkeySecretHelper.evolveSecret(7753432L));
    }
}

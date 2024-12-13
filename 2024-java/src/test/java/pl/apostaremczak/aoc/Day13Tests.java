package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day13Tests {
    private final Day13 day13 = new Day13("src/test/resources/13.txt");

    @Test
    public void testSolvePart1() {
        Long result = day13.solvePart1();
        assertEquals(480L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day13.solvePart2();
        assertEquals(875318608908L, result);
    }

    @Test
    public void testArcadeAffine() {
        Arcade arcade = new Arcade(new double[]{94, 22}, new double[]{34, 67}, new double[]{8400, 5400});
        Arcade affineArcade = arcade.affine();
        assertEquals(10000000008400L, affineArcade.prizeLocation()[0]);
        assertEquals(10000000005400L, affineArcade.prizeLocation()[1]);
    }
}

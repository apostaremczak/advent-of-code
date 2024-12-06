package pl.apostaremczak.aoc.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Coord2DTests implements DirectionSupport {
    @Test
    public void testRotateRightAngle() {
        assertEquals(LEFT, TOP.rotateRightAngle(90));
        assertEquals(BOTTOM, LEFT.rotateRightAngle(90));
        assertEquals(RIGHT, BOTTOM.rotateRightAngle(90));
        assertEquals(TOP, RIGHT.rotateRightAngle(90));

        assertEquals(RIGHT, TOP.rotateRightAngle(-90));

        assertEquals(BOTTOM, TOP.rotateRightAngle(180));
        assertEquals(RIGHT, RIGHT.rotateRightAngle(360));
    }

    @Test
    public void testFailedRotateRightAngle() {
        assertThrows(AssertionError.class, () -> TOP.rotateRightAngle(45));
    }
}

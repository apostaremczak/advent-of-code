package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;
import pl.apostaremczak.aoc.util.Coord2D;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day14Tests {
    private final Day14 day14 = new Day14("src/test/resources/14.txt", 7, 11);

    @Test
    public void testSolvePart1() {
        Long result = day14.solvePart1();
        assertEquals(12L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day14.solvePart2();
        assertEquals(6668L, result);
    }

    @Test
    public void testRobot() {
        Robot robotResult = Robot.fromString("p=7,6 v=-1,-3");
        Robot expectedResult = new Robot(new Coord2D(6, 7), new Coord2D(-3, -1));
        assertEquals(expectedResult, robotResult);
    }

    @Test
    public void testRobotMoveTimes() {
        Robot robot = Robot.fromString("p=2,4 v=2,-3");
        assertEquals(new Coord2D(1, 4), robot.moveTimes(1, 7, 11).currentPosition());
        assertEquals(new Coord2D(5, 6), robot.moveTimes(2, 7, 11).currentPosition());

        Robot result = robot.moveTimes(5, 7, 11);
        Coord2D expectedPosition = new Coord2D(3, 1);
        assertEquals(expectedPosition, result.currentPosition());
    }
}

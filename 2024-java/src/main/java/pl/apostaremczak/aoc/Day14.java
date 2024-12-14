package pl.apostaremczak.aoc;

import pl.apostaremczak.aoc.util.Coord2D;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day14 extends PuzzleSolution {
    private final Integer numRows;
    private final Integer numColumns;

    public Day14(String inputFilename, Integer numRows, Integer numColumns) {
        super(inputFilename);
        this.numRows = numRows;
        this.numColumns = numColumns;
    }

    @Override
    public Long solvePart1() {
        List<Robot> movedRobots = Arrays.stream(this.inputLines)
                .map(Robot::fromString)
                .map(robot -> robot.moveTimes(100, this.numRows, this.numColumns))
                .toList();
        int rowDivider = this.numRows / 2;
        int colDivider = this.numColumns / 2;
        Stream<Robot> robotsFirstQuadrant = movedRobots.stream()
                .filter(robot -> robot.currentPosition().row() < rowDivider && robot.currentPosition().column() < colDivider);
        Stream<Robot> robotsSecondQuadrant = movedRobots.stream()
                .filter(robot -> robot.currentPosition().row() < rowDivider && robot.currentPosition().column() > colDivider);
        Stream<Robot> robotsThirdQuadrant = movedRobots.stream()
                .filter(robot -> robot.currentPosition().row() > rowDivider && robot.currentPosition().column() < colDivider);

        Stream<Robot> robotsFourthQuadrant = movedRobots.stream()
                .filter(robot -> robot.currentPosition().row() > rowDivider && robot.currentPosition().column() > colDivider);

        return robotsFirstQuadrant.count() * robotsSecondQuadrant.count() * robotsThirdQuadrant.count() * robotsFourthQuadrant.count();
    }


    @Override
    public Long solvePart2() {
        // Answer 2960 was definitely too low
        int startIteration = 2960;
        List<Robot> robots = Arrays.stream(this.inputLines).map(Robot::fromString).map(r -> r.moveTimes(startIteration, this.numRows, this.numColumns)).toList();

        for (int iteration = startIteration + 1; iteration < 10000; iteration++) {
            robots = robots.stream().map(r -> r.move(this.numRows, this.numColumns)).toList();
            List<Coord2D> positions = robots.stream().map(Robot::currentPosition).toList();
            List<Integer> xs = positions.stream().map(Coord2D::row).toList();
            boolean formsALine = xs.stream().anyMatch(x -> Collections.frequency(xs, x) >= 30);
            if (formsALine) {
                System.out.println("---------- LAYOUT AFTER " + iteration + " MOVES -----------");
                printBathroomLayout(new HashSet<>(positions));
                System.out.println("\n\n\n");
            }

        }

        return 6668L;
    }

    private void printBathroomLayout(Set<Coord2D> positions) {
        for (int rowIdx = 0; rowIdx < this.numRows; rowIdx++) {
            StringBuilder rowRepresentation = new StringBuilder();
            for (int colIdx = 0; colIdx < this.numColumns; colIdx++) {
                if (positions.contains(new Coord2D(rowIdx, colIdx))) {
                    rowRepresentation.append("#");
                } else {
                    rowRepresentation.append(".");
                }
            }
            System.out.println(rowRepresentation);
        }
    }

    public static void main(String[] args) {
        Day14 day14 = new Day14("src/main/resources/14.txt", 103, 101);
        Long part1Solution = day14.solvePart1();
        System.out.println("Part 1: " + part1Solution);
        Long part2Solution = day14.solvePart2();
        System.out.println("Part 2: " + part2Solution);
    }
}

record Robot(Coord2D currentPosition, Coord2D velocity) {
    public static Robot fromString(String positionAndVelocity) {
        Pattern robotPattern = Pattern.compile("p=((\\d+),(\\d+)) v=(([-\\d]+),([-\\d]+))");
        Matcher robotMatcher = robotPattern.matcher(positionAndVelocity);
        Robot robot = null;
        while (robotMatcher.find()) {
            // y represents the number of tiles from the top wall = ROW
            //  x represents the number of tiles the robot is from the left wall = COLUMN
            int xPosition = Integer.parseInt(robotMatcher.group(3));
            int yPosition = Integer.parseInt(robotMatcher.group(2));
            // Positive x means the robot is moving to the right
            // positive y means the robot is moving down.
            int xVelocity = Integer.parseInt(robotMatcher.group(6));
            int yVelocity = Integer.parseInt(robotMatcher.group(5));
            robot = new Robot(new Coord2D(xPosition, yPosition), new Coord2D(xVelocity, yVelocity));
        }
        assert robot != null : "Could not parse robot details from " + positionAndVelocity;
        return robot;
    }

    public Robot moveTimes(Integer n, Integer numRows, Integer numColumns) {
        // p + n * v;
        Coord2D newPosition = currentPosition.add(velocity.scalarMultiplication(n));
        // Wrap the position around the map
        Coord2D wrappedPosition = new Coord2D(mod(newPosition.row(), numRows), mod(newPosition.column(), numColumns));

        return new Robot(wrappedPosition, velocity);
    }

    public Robot move(Integer numRows, Integer numColumns) {
        // p + v;
        Coord2D newPosition = currentPosition.add(velocity);
        // Wrap the position around the map
        Coord2D wrappedPosition = new Coord2D(mod(newPosition.row(), numRows), mod(newPosition.column(), numColumns));

        return new Robot(wrappedPosition, velocity);
    }

    /**
     * Positive-only modulo
     * mod(-2, 7) -> 5
     */
    private Integer mod(Integer a, Integer base) {
        int result = a % base;
        while (result < 0) {
            result += base;
        }
        return result;
    }
}

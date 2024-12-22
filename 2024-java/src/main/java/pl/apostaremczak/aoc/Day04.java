package pl.apostaremczak.aoc;

import pl.apostaremczak.aoc.util.Coord2D;
import pl.apostaremczak.aoc.util.DirectionSupport;
import pl.apostaremczak.aoc.util.Map2D;

import java.util.*;

public class Day04 extends PuzzleSolution implements DirectionSupport {
    private final Map2D<Character> InputMap2D;

    public Day04(String inputFilename) {
        super(inputFilename);

        this.InputMap2D = Map2D.fromStringInputLines(inputLines);
    }

    public static void main(String[] args) {
        long startTotal = System.currentTimeMillis();
        Day04 day = new Day04("src/main/resources/04.txt");

        long startPart1 = System.currentTimeMillis();
        Long part1Solution = day.solvePart1();
        long endPart1 = System.currentTimeMillis();
        System.out.println("Part 1: " + part1Solution + " (Time: " + (endPart1 - startPart1) + " ms)");

        long startPart2 = System.currentTimeMillis();
        Long part2Solution = day.solvePart2();
        long endPart2 = System.currentTimeMillis();
        System.out.println("Part 2: " + part2Solution + " (Time: " + (endPart2 - startPart2) + " ms)");

        long endTotal = System.currentTimeMillis();
        System.out.println("Total time: " + (endTotal - startTotal) + " ms");
    }

    @Override
    public Long solvePart1() {
        List<Coord2D> xLocations = findAllLocationsOf('X');

        int xmasCount = 0;

        for (Coord2D location : xLocations) {
            // For all 8 locations around, check if any of them create XMAS
            for (Coord2D direction : Directions2D) {
                Coord2D[] ray = new Coord2D[]{
                        location.add(direction),
                        location.add(direction.scalarMultiplication(2)),
                        location.add(direction.scalarMultiplication(3))
                };
                if (isMasAt(ray)) {
                    xmasCount++;
                }
            }
        }
        return (long) xmasCount;
    }

    @Override
    public Long solvePart2() {
        List<Coord2D> aLocations = findAllLocationsOf('A');
        int xmasCount = 0;

        for (Coord2D location : aLocations) {
            boolean firstRay = isMsAt(new Coord2D[]{location.add(TOP_LEFT), location.add(BOTTOM_RIGHT)});
            boolean secondRay = isMsAt(new Coord2D[]{location.add(TOP_RIGHT), location.add(BOTTOM_LEFT)});

            if (firstRay && secondRay) {
                xmasCount++;
            }
        }

        return (long) xmasCount;
    }

    private boolean isMasAt(Coord2D[] ray) {
        if (InputMap2D.safeGetAt(ray[0]).orElse(' ') != 'M') {
            return false;
        }
        if (InputMap2D.safeGetAt(ray[1]).orElse(' ') != 'A') {
            return false;
        }
        return InputMap2D.safeGetAt(ray[2]).orElse(' ') == 'S';
    }

    private boolean isMsAt(Coord2D[] ray) {
        Set<Character> firstRayChars = new HashSet<>(Arrays.asList(
                InputMap2D.safeGetAt(ray[0]).orElse(' '),
                InputMap2D.safeGetAt(ray[1]).orElse(' ')
        ));
        return firstRayChars.contains('M') && firstRayChars.contains('S');
    }

    private List<Coord2D> findAllLocationsOf(Character letter) {
        List<Coord2D> locations = new ArrayList<>();

        for (int row = 0; row <= InputMap2D.MAX_ROW_INDEX; row++) {
            for (int col = 0; col <= InputMap2D.MAX_COLUMN_INDEX; col++) {
                if (InputMap2D.internal[row][col] == letter) {
                    locations.add(new Coord2D(row, col));
                }
            }
        }

        return locations;
    }
}

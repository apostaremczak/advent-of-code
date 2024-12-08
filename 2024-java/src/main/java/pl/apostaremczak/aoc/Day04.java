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

    public static void main(String[] args) {
        Day04 day04 = new Day04("src/main/resources/04.txt");
        Long part1Solution = day04.solvePart1();
        System.out.println("Part 1: " + part1Solution);
        Long part2Solution = day04.solvePart2();
        System.out.println("Part 2: " + part2Solution);
    }
}

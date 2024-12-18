package pl.apostaremczak.aoc;

import pl.apostaremczak.aoc.util.Coord2D;
import pl.apostaremczak.aoc.util.DirectionSupport;

import java.util.*;

public class Day06 extends PuzzleSolution implements DirectionSupport {
    final private Coord2D startingPosition;
    final private Coord2D startingDirection;
    final private HashSet<Coord2D> Obstacles = new HashSet<>();
    final private Integer MaxRow;
    final private Integer MaxColumn;

    public Day06(String inputFilename) {
        super(inputFilename);

        // Parse the input map; only keep track of the obstacle locations, since everything else is empty
        Coord2D position = null;
        Coord2D direction = null;
        this.MaxRow = this.inputLines.length - 1;
        this.MaxColumn = this.inputLines[0].length() - 1;

        for (int rowIndex = 0; rowIndex <= MaxRow; rowIndex++) {
            char[] row = this.inputLines[rowIndex].toCharArray();
            for (int colIndex = 0; colIndex <= MaxColumn; colIndex++) {
                switch (row[colIndex]) {
                    case '#':
                        Obstacles.add(new Coord2D(rowIndex, colIndex));
                        break;
                    case '^':
                        position = new Coord2D(rowIndex, colIndex);
                        direction = DirectionSupport.TOP;
                        break;
                }
            }
        }
        assert position != null && direction != null : "Start position and direction not found";

        this.startingPosition = position;
        this.startingDirection = direction;
    }

    @Override
    public Long solvePart1() {
        HashSet<Coord2D> visitedSpots = new HashSet<>();
        Coord2D position = startingPosition;
        Coord2D direction = startingDirection;

        while (isWithinMapBounds(position)) {
            visitedSpots.add(position);
            Coord2D nextPosition = position.add(direction);
            if (Obstacles.contains(nextPosition)) {
                // Rotate right to avoid bumping into objects
                direction = direction.rotateRightAngle(-90);
                nextPosition = position.add(direction);
            }
            position = nextPosition;
        }

        return (long) visitedSpots.size();
    }

    @Override
    public Long solvePart2() {
        HashSet<Coord2D> visitedSpots = new HashSet<>();
        Coord2D position = startingPosition;
        Coord2D direction = startingDirection;
        HashSet<Coord2D> guardTraps = new HashSet<>();

        // For each spot currently occupied by the guard, try placing an obstacle at their next position and check if they could get stuck in a loop
        while (isWithinMapBounds(position) && !Obstacles.contains(position)) {
            visitedSpots.add(position);
            Coord2D nextPosition = position.add(direction);

            // When an obstacle is encountered, turn right until the path is clear
            if (Obstacles.contains(nextPosition)) {
                HashSet<Coord2D> exploredNextPositions = new HashSet<>(List.of(nextPosition));
                boolean foundNextSpot = false;
                while (!foundNextSpot) {
                    direction = direction.rotateRightAngle(-90);
                    nextPosition = position.add(direction);
                    exploredNextPositions.add(nextPosition);
                    if (!Obstacles.contains(nextPosition)) {
                        foundNextSpot = true;
                    }
                    if (exploredNextPositions.size() == 4) {
                        throw new RuntimeException("Stuck in a single spot");
                    }
                }
            }

            // Try placing an obstacle at the next position, but only if it's not a spot that was previously visited
            // (otherwise the guard couldn't have passed it to get to the current spot)
            if (!visitedSpots.contains(nextPosition) && willTrapGuard(position, direction, nextPosition)) {
                guardTraps.add(nextPosition);
            }
            position = nextPosition;
        }
        return (long) guardTraps.size();
    }

    public boolean willTrapGuard(Coord2D guardPosition, Coord2D guardDirection, Coord2D obstaclePosition) {
        List<Coord2D> turns = new ArrayList<>();
        Coord2D position = guardPosition;
        Coord2D direction = guardDirection;
        HashSet<Coord2D> obstacles = new HashSet<>(Obstacles);
        obstacles.add(obstaclePosition);

        while (isWithinMapBounds(position)) {
            Coord2D nextPosition = position.add(direction);
            if (obstacles.contains(nextPosition)) {
                // We've already turned right at this position, so we've got a loop
                if (turns.contains(position)) {
                    return true;
                }

                // Turn right until the path is clear
                HashSet<Coord2D> exploredNextPositions = new HashSet<>(List.of(nextPosition));
                boolean foundNextSpot = false;
                while (!foundNextSpot) {
                    direction = direction.rotateRightAngle(-90);
                    nextPosition = position.add(direction);
                    exploredNextPositions.add(nextPosition);
                    if (!obstacles.contains(nextPosition)) {
                        foundNextSpot = true;
                        // Take note of when we made this turn to detect loops
                        turns.add(position);
                    }
                    if (exploredNextPositions.size() == 4 && !foundNextSpot) {
                        // Single-spot loop
                        return true;
                    }
                }
            }
            position = nextPosition;
        }
        return false;
    }

    private boolean isWithinMapBounds(Coord2D position) {
        return position.row() >= 0 && position.column() >= 0 && position.row() <= MaxRow && position.column() <= MaxColumn;
    }

    public static void main(String[] args) {
        Day06 day06 = new Day06("src/main/resources/06.txt");
        Long part1Solution = day06.solvePart1();
        System.out.println("Part 1: " + part1Solution);
        Long part2Solution = day06.solvePart2();
        System.out.println("Part 2: " + part2Solution);
    }

    /**
     * Used for debugging; prints a visualization of the current state of the map.
     */
    private void printMap(HashSet<Coord2D> obstacles, Coord2D guardPosition, Coord2D guardDirection) {
        String guardMarker = DirectionSupport.directionToString(guardDirection);
        for (int row = 0; row <= MaxRow; row++) {
            for (int col = 0; col <= MaxColumn; col++) {
                Coord2D current = new Coord2D(row, col);
                if (obstacles.contains(current)) {
                    System.out.print('#');
                } else if (guardPosition.equals(current)) {
                    System.out.print(guardMarker);
                } else {
                    System.out.print('.');
                }
            }
            System.out.print('\n');
        }
    }
}

package pl.apostaremczak.aoc;

import pl.apostaremczak.aoc.util.CharacterMap2d;
import pl.apostaremczak.aoc.util.Coord2D;

import java.util.*;

public class Day20 extends PuzzleSolution {
    Coord2D StartPosition;
    Coord2D EndPosition;
    Long ShortestLegalPath = Long.MAX_VALUE;
    CharacterMap2d Maze;
    Long SavedTimeThreshold;
    Map<Coord2D, Long> DistancesFromStart;
    Map<Coord2D, Long> DistancesFromEnd;


    public Day20(String inputFilename, Long savedTimeThreshold) {
        super(inputFilename);
        Maze = CharacterMap2d.fromStringInputLines(inputLines, '#');
        StartPosition = Maze.findFirst('S').get();
        EndPosition = Maze.findFirst('E').get();
        SavedTimeThreshold = savedTimeThreshold;

        DistancesFromStart = Maze.getDistancesFrom(StartPosition);
        DistancesFromEnd = Maze.getDistancesFrom(EndPosition);
        ShortestLegalPath = DistancesFromStart.get(EndPosition);
    }

    private Long countShortcuts(Integer maxShortcutLength) {
        long shorterPathCount = 0L;

        // Iterate over all non-wall points on the grid
        for (int rowIdx = 0; rowIdx <= Maze.MAX_ROW_INDEX; rowIdx++) {
            for (int colIdx = 0; colIdx <= Maze.MAX_COLUMN_INDEX; colIdx++) {
                Coord2D currentPosition = new Coord2D(rowIdx, colIdx);
                if (!Maze.isWall(currentPosition)) {
                    // Check all the neighbors and check if the tile after the neighbor is free
                    for (Coord2D teleport : Maze.getManhattanClosedBallPoints(currentPosition, maxShortcutLength)) {
                        if (!Maze.isWall(teleport)) {
                            // Teleporting from p to q through a cheat
                            // d(S, p) + d(p, q) + d(q, E)
                            long cheatedDistance = DistancesFromStart.get(currentPosition) + currentPosition.manhattanDistanceFrom(teleport) + DistancesFromEnd.get(teleport);
                            if (ShortestLegalPath - cheatedDistance >= SavedTimeThreshold) {
                                shorterPathCount++;
                            }
                        }
                    }
                }
            }
        }
        return shorterPathCount;
    }

    @Override
    public Long solvePart1() {
        return countShortcuts(2);
    }

    @Override
    public Long solvePart2() {
        return countShortcuts(20);
    }

    public static void main(String[] args) {
        Day20 day20 = new Day20("src/main/resources/20.txt", 100L);
        Long part1Solution = day20.solvePart1();
        System.out.println("Part 1: " + part1Solution);
        Long part2Solution = day20.solvePart2();
        System.out.println("Part 2: " + part2Solution);
    }
}


package pl.apostaremczak.aoc;

import pl.apostaremczak.aoc.util.Coord2D;
import pl.apostaremczak.aoc.util.Map2D;

import java.util.*;

public class Day20 extends PuzzleSolution {
    Coord2D StartPosition;
    Coord2D EndPosition;
    Map<CheatPosition, Long> CheatedPathLengths = new HashMap<>();
    Long ShortestLegalPath = Long.MAX_VALUE;
    //    Set<Coord2D> Walls;
    Map2D<Character> Maze;
    Long SavedTimeThreshold;
    Set<CheatPosition> ExploredCheats = new HashSet<>();

    public Day20(String inputFilename, Long savedTimeThreshold) {
        super(inputFilename);
        Maze = Map2D.fromStringInputLines(inputLines);
        StartPosition = Maze.findFirst('S').get();
        EndPosition = Maze.findFirst('E').get();
        SavedTimeThreshold = savedTimeThreshold;
    }

    @Override
    public Long solvePart1() {
        findRaceTrackLength(new LinkedList<>(), StartPosition, Optional.empty());
        // How many cheats would save you at least 100 picoseconds?
        long result = 0;
        for (long cheatedPathLength : CheatedPathLengths.values()) {
            if (ShortestLegalPath - cheatedPathLength >= SavedTimeThreshold) {
                result++;
            }
        }
        return result;
    }

    @Override
    public Long solvePart2() {
        return 0L;
    }

    private Boolean isWall(Coord2D position) {
        return Maze.safeGetAt(position).orElse('X').equals('#');
    }

    // DFS for finding track lengths
    private void findRaceTrackLength(LinkedList<Coord2D> visited, Coord2D currentNode, Optional<CheatPosition> cheatPosition) {
        visited.add(currentNode);

        if (currentNode.equals(EndPosition)) {
            long currentPathLength = visited.size();
            if (cheatPosition.isEmpty()) {
                if (currentPathLength < ShortestLegalPath) {
                    ShortestLegalPath = currentPathLength;
                }
            } else {
                CheatedPathLengths.put(cheatPosition.get(), currentPathLength);
            }
        }

        for (Coord2D neighbor : currentNode.getStraightSurrounding()) {
            if (!visited.contains(neighbor) && Maze.isWithinBounds(neighbor)) {
                // Try cheating if it hasn't been done already and if the next tile after the wall is not a wall itself
                if (isWall(neighbor) && cheatPosition.isEmpty()) {
                    Coord2D direction = neighbor.minus(currentNode);
                    Coord2D afterWallNode = neighbor.add(direction);
                    if (Maze.isWithinBounds(afterWallNode) && !isWall(afterWallNode) && !visited.contains(afterWallNode)) {
                        LinkedList<Coord2D> cheatVisited = new LinkedList<>(visited);
                        cheatVisited.add(neighbor);
                        CheatPosition cheat = new CheatPosition(neighbor, afterWallNode);
                        if (!ExploredCheats.contains(cheat)) {
                            ExploredCheats.add(cheat);
                            findRaceTrackLength(cheatVisited, afterWallNode, Optional.of(cheat));
                        }
                    }
                }
                // Turn and proceed normally
                if (!isWall(neighbor)) {
                    findRaceTrackLength(new LinkedList<>(visited), neighbor, cheatPosition);
                }
            }
        }
    }

    public static void main(String[] args) {
        Day20 day20 = new Day20("src/main/resources/20.txt", 100L);
        Long part1Solution = day20.solvePart1();
        System.out.println("Part 1: " + part1Solution);
        Long part2Solution = day20.solvePart2();
        System.out.println("Part 2: " + part2Solution);
    }
}

record CheatPosition(Coord2D first, Coord2D second) {

}


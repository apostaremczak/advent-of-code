package pl.apostaremczak.aoc;

import pl.apostaremczak.aoc.util.*;

import java.util.*;
import java.util.stream.Stream;

public class Day16 extends PuzzleSolution {
    public Day16(String inputFilename) {
        super(inputFilename);
    }

    @Override
    public Long solvePart1() {
        CharacterMap2d mazeMap = CharacterMap2d.fromStringInputLines(inputLines, '#');
        Coord2D startPosition = mazeMap.findFirst('S').get();
        Coord2D endPosition = mazeMap.findFirst('E').get();
        Map<DirectedMovement, Long> distancesFromStart = mazeMap.getDirectedDistancesFrom(new DirectedMovement(startPosition, DirectionSupport.RIGHT), 1000);
        Stream<DirectedMovement> finishingMoves = distancesFromStart.keySet().stream().filter(m -> m.position().equals(endPosition));
        return finishingMoves.map(distancesFromStart::get).mapToLong(l -> l).min().getAsLong();
    }

    @Override
    public Long solvePart2() {
        CharacterMap2d mazeMap = CharacterMap2d.fromStringInputLines(inputLines, '#');
        Coord2D startPosition = mazeMap.findFirst('S').get();
        Coord2D endPosition = mazeMap.findFirst('E').get();
        Map<DirectedMovement, Long> distancesFromStart = mazeMap.getDirectedDistancesFrom(new DirectedMovement(startPosition, DirectionSupport.RIGHT), 1000);
        Map<DirectedMovement, Long> distancesFromEndLeft = mazeMap.getDirectedDistancesFrom(new DirectedMovement(endPosition, DirectionSupport.LEFT), 1000);
        Map<DirectedMovement, Long> distancesFromEndBottom = mazeMap.getDirectedDistancesFrom(new DirectedMovement(endPosition, DirectionSupport.BOTTOM), 1000);

        Stream<DirectedMovement> finishingMoves = distancesFromStart.keySet().stream().filter(m -> m.position().equals(endPosition));
        // Make sure this measurement includes both ends
        long shortestPathLength = finishingMoves.map(distancesFromStart::get).mapToLong(l -> l).min().getAsLong() + 1;

        Set<Coord2D> tilesOnShortestPaths = new HashSet<>();

        // Tile p is on one of the shortest paths, if:
        // d(S, p) + d(p, E) + 1 = shortest path length
        for (Iterator<Coord2D> it = mazeMap.positionIterator(); it.hasNext(); ) {
            Coord2D position = it.next();
            if (mazeMap.isWall(position)) {
                continue;
            }
            List<DirectedMovement> movesFromStart = distancesFromStart.keySet().stream().filter(m -> m.position().equals(position)).toList();
            List<DirectedMovement> movesFromEndLeft = distancesFromEndLeft.keySet().stream().filter(m -> m.position().equals(position)).toList();
            List<DirectedMovement> movesFromEndBottom = distancesFromEndBottom.keySet().stream().filter(m -> m.position().equals(position)).toList();
            // Skip if the tile is not connected to the end tile
            if (movesFromEndLeft.isEmpty() && movesFromEndBottom.isEmpty()) {
                continue;
            }

            for (DirectedMovement moveFromStart : movesFromStart) {
                Coord2D directionFromStart = moveFromStart.direction();
                long distanceFromStart = distancesFromStart.get(moveFromStart);
                // Skip if the tile is too far from the start anyway
                if (distanceFromStart > shortestPathLength) {
                    continue;
                }
                // Check if this tile can be reached from the end <-E
                if (isOnShortestPath(distancesFromEndLeft, shortestPathLength, movesFromEndLeft, directionFromStart, distanceFromStart)) {
                    tilesOnShortestPaths.add(position);
                } else
                    // Check if this tile can be reached from the end
                    // E
                    // |
                    // v
                    if (isOnShortestPath(distancesFromEndBottom, shortestPathLength, movesFromEndBottom, directionFromStart, distanceFromStart)) {
                        tilesOnShortestPaths.add(position);
                    }
            }
        }
        return (long) tilesOnShortestPaths.size();
    }

    private boolean isOnShortestPath(Map<DirectedMovement, Long> distancesFromEnd, long shortestPathLength, List<DirectedMovement> movesFromEnd, Coord2D directionFromStart, long distanceFromStart) {
        for (DirectedMovement moveFromEndLeft : movesFromEnd) {
            Coord2D directionFromEndLeft = moveFromEndLeft.direction();
            long distanceFromEndLeft = distancesFromEnd.get(moveFromEndLeft);
            if (distanceFromEndLeft > shortestPathLength) {
                continue;
            }
            int pointDifference = Integer.MAX_VALUE;
            if (directionFromStart.rotateRightAngle(180).equals(directionFromEndLeft)) {
                pointDifference = 1;
            } else if (directionFromStart.rotateRightAngle(90).equals(directionFromEndLeft) || directionFromStart.rotateRightAngle(-90).equals(directionFromEndLeft)) {
                pointDifference = 1001;
            }
            if ((distanceFromStart + pointDifference + distanceFromEndLeft) == shortestPathLength) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Day16 day16 = new Day16("src/main/resources/16.txt");
        Long part1Solution = day16.solvePart1();
        System.out.println("Part 1: " + part1Solution);
        Long part2Solution = day16.solvePart2();
        System.out.println("Part 2: " + part2Solution);
    }
}

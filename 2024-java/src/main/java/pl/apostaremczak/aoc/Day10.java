package pl.apostaremczak.aoc;

import pl.apostaremczak.aoc.util.Coord2D;

import java.util.*;

public class Day10 extends PuzzleSolution {
    Set<Coord2D> StartingPositions;
    Map<Coord2D, List<Coord2D>> ReachableTops;

    public Day10(String inputFilename) {
        super(inputFilename);

        Set<Coord2D> startingPositions = new HashSet<>();
        Map<Coord2D, Integer> topographicMap = new HashMap<>();

        for (int rowIdx = 0; rowIdx < this.inputLines.length; rowIdx++) {
            String row = this.inputLines[rowIdx];
            for (int columnIdx = 0; columnIdx < row.length(); columnIdx++) {
                int height = Character.getNumericValue(row.toCharArray()[columnIdx]);
                Coord2D coord = new Coord2D(rowIdx, columnIdx);
                if (height == 0) {
                    startingPositions.add(coord);
                }
                topographicMap.put(coord, height);
            }
        }

        this.StartingPositions = startingPositions;
        Map<Coord2D, List<Coord2D>> reachableTops = new HashMap<>();
        for (Coord2D startingPosition : startingPositions) {
            List<Coord2D> reachableFromHere = this.getAllReachableTops(startingPosition, topographicMap);
            reachableTops.put(startingPosition, reachableFromHere);
        }

        this.ReachableTops = reachableTops;
    }

    @Override
    public Long solvePart1() {
        long result = 0;

        for (Coord2D startingPoint : this.StartingPositions) {
            result += this.getTrailheadScore(startingPoint);
        }

        return result;
    }

    @Override
    public Long solvePart2() {
        long result = 0;

        for (Coord2D startingPoint : this.StartingPositions) {
            result += this.getTrailheadRating(startingPoint);
        }

        return result;
    }


    /**
     * BFS: Count the number of distinct mountain tops of height 9 that can be reached from the starting point
     */
    public Integer getTrailheadScore(Coord2D startingPoint) {
       return new HashSet<>(this.ReachableTops.get(startingPoint)).size();
    }

    /**
     * BFS: Counts the number of distinct paths from a starting point to 9s
     */
    public Integer getTrailheadRating(Coord2D startingPoint) {
        return this.ReachableTops.get(startingPoint).size();
    }

    /**
     * BFS to find all the coordinates of points of height 9 that can be reached from the starting point.
     *
     * @param startingPoint Coordinate with height 0.
     * @return List of height 9; if more than one path exists, the point will be duplicated.
     */
    public List<Coord2D> getAllReachableTops(Coord2D startingPoint, Map<Coord2D, Integer> topographicMap) {
        List<Coord2D> reachableTops = new ArrayList<>();

        Queue<Coord2D> queue = new LinkedList<>();
        queue.offer(startingPoint);

        while (!queue.isEmpty()) {
            Coord2D current = queue.poll();
            int currentValue = topographicMap.get(current);

            if (currentValue == 9) {
                reachableTops.add(current);
                continue;
            }

            for (Coord2D neighbor : current.getStraightSurrounding()) {
                int neighborValue = topographicMap.getOrDefault(neighbor, -1);
                if (neighborValue - currentValue == 1) {
                    queue.offer(neighbor);
                }
            }
        }

        return reachableTops;
    }

    public static void main(String[] args) {
        Day10 day10 = new Day10("src/main/resources/10.txt");
        Long part1Solution = day10.solvePart1();
        System.out.println("Part 1: " + part1Solution);
        Long part2Solution = day10.solvePart2();
        System.out.println("Part 2: " + part2Solution);
    }
}

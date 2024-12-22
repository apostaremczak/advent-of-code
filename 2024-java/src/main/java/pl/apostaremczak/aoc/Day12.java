package pl.apostaremczak.aoc;

import pl.apostaremczak.aoc.util.Coord2D;
import pl.apostaremczak.aoc.util.DirectionSupport;

import java.util.*;

public class Day12 extends PuzzleSolution {
    Farm farm;

    public Day12(String inputFilename) {
        super(inputFilename);
        Farm farm = new Farm(inputLines);
        farm.fenceAll();
        this.farm = farm;
    }

    public static void main(String[] args) {
        long startTotal = System.currentTimeMillis();
        Day12 day = new Day12("src/main/resources/12.txt");

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
        return this.farm.totalPrice();
    }

    @Override
    public Long solvePart2() {
        return this.farm.bulkPrice();
    }
}

class Farm {
    Map<Coord2D, Character> map;
    List<FencedPlot> fences;

    public Farm(String[] inputLines) {
        this.map = new HashMap<>();
        for (int rowIdx = 0; rowIdx < inputLines.length; rowIdx++) {
            char[] row = inputLines[rowIdx].toCharArray();
            for (int colIdx = 0; colIdx < row.length; colIdx++) {
                Coord2D fieldCoord = new Coord2D(rowIdx, colIdx);
                Character fieldLetter = row[colIdx];
                map.put(fieldCoord, fieldLetter);
            }
        }
        this.fences = new ArrayList<>();
    }

    public List<Coord2D> fence(List<Coord2D> visited, Coord2D startNode) {
        List<Coord2D> updatedVisited = new LinkedList<>(visited);

        Set<Coord2D> currentField = new HashSet<>();
        LinkedList<Coord2D> queue = new LinkedList<>();
        queue.add(startNode);
        long emptySides = 0L;

        while (!queue.isEmpty()) {
            Coord2D node = queue.poll();
            updatedVisited.add(node);
            currentField.add(node);
            Character color = this.map.get(node);
            for (Coord2D neighbor : node.getStraightSurrounding()) {
                if (this.map.getOrDefault(neighbor, '#').equals(color)) {
                    if (!updatedVisited.contains(neighbor) && !queue.contains(neighbor)) {
                        queue.add(neighbor);
                    }
                } else {
                    emptySides++;
                }
            }
        }

        FencedPlot plot = new FencedPlot(currentField, emptySides);
        this.fences.add(plot);

        return updatedVisited;
    }

    public void fenceAll() {
        List<Coord2D> visited = new LinkedList<>();
        for (Coord2D startNode : this.map.keySet()) {
            if (!visited.contains(startNode)) {
                visited = fence(visited, startNode);
            }
        }
    }

    public Long totalPrice() {
        return this.fences.stream().map(FencedPlot::price).mapToLong(l -> l).sum();
    }

    public Long bulkPrice() {
        return this.fences.stream().map(FencedPlot::bulkPrice).mapToLong(l -> l).sum();
    }
}

record FencedPlot(Set<Coord2D> fields, Long emptySides) implements DirectionSupport {
    private Long perimeter() {
        return this.emptySides;
    }

    private Long size() {
        return (long) fields.size();
    }

    public Long price() {
        return perimeter() * size();
    }

    /**
     * The number of sides equals the number of vertices (aka corners). Finds the number of corners in a given polygon.
     */
    public Long sides() {
        long corners = 0;

        for (Coord2D plot : this.fields) {
            boolean topOccupied = this.fields.contains(plot.add(TOP));
            boolean rightOccupied = this.fields.contains(plot.add(RIGHT));
            boolean bottomOccupied = this.fields.contains(plot.add(BOTTOM));
            boolean leftOccupied = this.fields.contains(plot.add(LEFT));

            // Concave
            // |_
            if (!leftOccupied & !bottomOccupied) {
                corners++;
            }
            // _|
            if (!bottomOccupied && !rightOccupied) {
                corners++;
            }
            // ┐
            if (!topOccupied && !rightOccupied) {
                corners++;
            }
            // ┌
            if (!topOccupied && !leftOccupied) {
                corners++;
            }

            boolean topRightOccupied = this.fields.contains(plot.add(TOP_RIGHT));
            boolean topLeftOccupied = this.fields.contains(plot.add(TOP_LEFT));
            boolean bottomRightOccupied = this.fields.contains(plot.add(BOTTOM_RIGHT));
            boolean bottomLeftOccupied = this.fields.contains(plot.add(BOTTOM_LEFT));

            // Convex
            // |_
            if (topOccupied && rightOccupied && !topRightOccupied) {
                corners++;
            }

            // _|
            if (topOccupied && leftOccupied && !topLeftOccupied) {
                corners++;
            }

            // ┐
            if (leftOccupied && bottomOccupied && !bottomLeftOccupied) {
                corners++;
            }

            // ┌
            if (rightOccupied && bottomOccupied && !bottomRightOccupied) {
                corners++;
            }
        }

        return corners;
    }

    public Long bulkPrice() {
        return sides() * size();
    }
}

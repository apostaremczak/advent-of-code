package pl.apostaremczak.aoc;

import java.util.*;
import java.util.stream.Collectors;

public class Day02 extends PuzzleSolution {
    private final HashSet<Long> AllowedIncreases = new HashSet<>(Arrays.asList(1L, 2L, 3L));
    private final HashSet<Long> AllowedDecreases = new HashSet<>(Arrays.asList(-1L, -2L, -3L));
    private final List<List<Long>> parsedInput;

    private static List<List<Long>> parseInput(String[] input) {
        return Arrays.stream(input).map(line -> Arrays.stream(line.split(" ")).map(Long::parseLong).collect(Collectors.toList())).collect(Collectors.toList());
    }

    public Day02(String inputFilename) {
        super(inputFilename);
        this.parsedInput = parseInput(inputLines);
    }

    @Override
    public Long solvePart1() {
        return parsedInput.stream().filter(this::isSafe).count();
    }

    @Override
    public Long solvePart2() {
        return parsedInput.stream().filter(line -> isSafe(line) || couldBeSafe(line)).count();
    }

    private boolean isSafe(List<Long> line) {
        Collection<Long> differences = new ArrayList<>();
        for (int i = 0; i < line.size() - 1; i++) {
            differences.add(line.get(i + 1) - line.get(i));
        }
        HashSet<Long> uniqueDifferences = new HashSet<>(differences);
        // SetA.containsAll(SetB) returns true is B is a subset of A
        return AllowedIncreases.containsAll(uniqueDifferences) || AllowedDecreases.containsAll(uniqueDifferences);
    }

    private boolean couldBeSafe(List<Long> line) {
        for (int i = 0; i < line.size(); i++) {
            List<Long> subLine = new ArrayList<>(line);
            subLine.remove(i);
            if (isSafe(subLine)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Day02 day02 = new Day02("src/main/resources/02.txt");
        Long part1Solution = day02.solvePart1();
        System.out.println("Part 1: " + part1Solution);
        Long part2Solution = day02.solvePart2();
        System.out.println("Part 2: " + part2Solution);
    }
}

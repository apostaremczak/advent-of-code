package pl.apostaremczak.aoc;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Day07 extends PuzzleSolution {
    public Day07(String inputFilename) {
        super(inputFilename);
    }

    public static void main(String[] args) {
        long startTotal = System.currentTimeMillis();
        Day07 day = new Day07("src/main/resources/07.txt");

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
        return Arrays.stream(inputLines)
                .map(Equation::new)
                .filter(Equation::canPlaceOperators)
                .map(eq -> eq.Result)
                .mapToLong(i -> i)
                .sum();
    }

    @Override
    public Long solvePart2() {
        return Arrays.stream(inputLines)
                .map(Equation::new)
                .filter(Equation::canPlaceOperatorsWithConcat)
                .map(eq -> eq.Result)
                .mapToLong(i -> i)
                .sum();
    }
}

class Equation {
    public final Long Result;
    private final LinkedList<Long> Elements;
    private final List<Long> ElementsIdx;
    private final Integer MaxIdx;

    public Equation(String line) {
        String[] splitLine = line.split(": ");
        this.Result = Long.parseLong(splitLine[0]);

        List<Long> numbers = Arrays.stream(splitLine[1].split(" ")).map(Long::parseLong).toList();
        this.Elements = new LinkedList<>(numbers);
        this.ElementsIdx = numbers;
        this.MaxIdx = numbers.size() - 1;
    }

    /**
     * DFS search for operator (+, *) placements.
     */
    private boolean canPlaceOperators(Long goal, Long currentValue, Integer currentIndex) {
        if (currentIndex > MaxIdx) {
            return goal.equals(currentValue);
        }
        if (currentValue > goal) {
            return false;
        }
        Long nextElement = ElementsIdx.get(currentIndex);
        return canPlaceOperators(goal, currentValue * nextElement, currentIndex + 1)
                || canPlaceOperators(goal, currentValue + nextElement, currentIndex + 1);
    }

    public boolean canPlaceOperators() {
        return canPlaceOperators(Result, Elements.getFirst(), 1);
    }

    /**
     * DFS search for operator (+, *, ||) placements.
     */
    private boolean canPlaceOperatorsWithConcat(Long goal, Long currentValue, Integer currentIndex) {
        if (currentIndex > MaxIdx) {
            return goal.equals(currentValue);
        }
        if (currentValue > goal) {
            return false;
        }
        Long nextElement = ElementsIdx.get(currentIndex);
        Long multiplicationBase = currentValue;
        if (currentValue == 0L) {
            multiplicationBase = 1L;
        }
        String concatenationBase = currentValue.toString();
        if (currentValue == 0L) {
            concatenationBase = "";
        }
        boolean canAdd = canPlaceOperatorsWithConcat(goal, currentValue + nextElement, currentIndex + 1);
        boolean canMultiply = canPlaceOperatorsWithConcat(goal, multiplicationBase * nextElement, currentIndex + 1);
        boolean canConcat = canPlaceOperatorsWithConcat(goal, Long.parseLong(concatenationBase + nextElement), currentIndex + 1);
        return canConcat || canMultiply || canAdd;
    }

    public boolean canPlaceOperatorsWithConcat() {
        return canPlaceOperatorsWithConcat(Result, 0L, 0);
    }
}

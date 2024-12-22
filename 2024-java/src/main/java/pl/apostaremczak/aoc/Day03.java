package pl.apostaremczak.aoc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 extends PuzzleSolution {
    public Day03(String inputFilename) {
        super(inputFilename);
    }

    public static void main(String[] args) {
        long startTotal = System.currentTimeMillis();
        Day03 day = new Day03("src/main/resources/03.txt");

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
        return findAndCalculate(fullRawInput, false);
    }

    @Override
    public Long solvePart2() {
        return findAndCalculate(fullRawInput, true);
    }

    private long findAndCalculate(String line, Boolean filtered) {
        long result = 0;
        long filteredResult = 0;

        Pattern multiplicationPattern = Pattern.compile("(mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\))");
        Matcher matcher = multiplicationPattern.matcher(line);
        boolean enabled = true;
        while (matcher.find()) {
            String expression = matcher.group(0);
            switch (expression) {
                case "do()":
                    enabled = true;
                    break;
                case "don't()":
                    enabled = false;
                    break;
                default:
                    long multResult = ((long) Integer.parseInt(matcher.group(2)) * Integer.parseInt(matcher.group(3)));
                    result += multResult;
                    if (enabled) {
                        filteredResult += multResult;
                    }
            }
        }
        if (filtered) {
            return filteredResult;
        }
        return result;
    }
}

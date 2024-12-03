package pl.apostaremczak.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 extends PuzzleSolution {
    public Day03(String inputFilename) {
        super(inputFilename);
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


    public static void main(String[] args) {
        Day03 day03 = new Day03("src/main/resources/03.txt");
        Long part1Solution = day03.solvePart1();
        System.out.println("Part 1: " + part1Solution);
        Long part2Solution = day03.solvePart2();
        System.out.println("Part 2: " + part2Solution);
    }
}

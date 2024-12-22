package pl.apostaremczak.aoc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day01 extends PuzzleSolution {
    List<List<Long>> parsedInput;

    public Day01(String inputFilename) {
        super(inputFilename);
        parsedInput = parseInput(inputLines);
    }

    public static void main(String[] args) {
        long startTotal = System.currentTimeMillis();
        Day01 day = new Day01("src/main/resources/01.txt");

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

    private List<List<Long>> parseInput(String[] input) {
        Pattern locationPattern = Pattern.compile("(\\d+)\\s+(\\d+)");
        List<Long> left = new ArrayList<>();
        List<Long> right = new ArrayList<>();

        for (String line : input) {
            Matcher matcher = locationPattern.matcher(line);
            // You need to call Matcher.matches() (or Matcher.find()) before you call m.group(),
            // otherwise it will say there aren't any matches (because it didn't look for any yet)
            if (matcher.matches()) {
                left.add(Long.parseLong(matcher.group(1)));
                right.add(Long.parseLong(matcher.group(2)));
            }
        }
        return Arrays.asList(left, right);
    }

    @Override
    public Long solvePart1() {
        Long[] left = parsedInput.get(0).toArray(new Long[0]);
        Arrays.sort(left);
        Long[] right = parsedInput.get(1).toArray(new Long[0]);
        Arrays.sort(right);

        assert left.length == right.length;

        long differences = 0L;
        for (int i = 0; i < left.length; i++) {
            differences += Math.abs(left[i] - right[i]);
        }

        return differences;
    }

    @Override
    public Long solvePart2() {
        Collection<Long> left = parsedInput.get(0);
        Collection<Long> right = parsedInput.get(1);

        // For each element in the left list, count how many times it appears in the right list
        // and multiply it by the element itself
        long result = 0L;
        for (long element : left) {
            long count = Collections.frequency(right, element);
            result += element * count;
        }

        return result;
    }
}

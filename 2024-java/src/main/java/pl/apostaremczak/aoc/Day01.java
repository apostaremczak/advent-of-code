package pl.apostaremczak.aoc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day01 implements PuzzleSolution {
    public Long solvePart1(String[] input) {
        List<List<Long>> parsedInput = parseInput(input);
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


    public Long solvePart2(String[] input) {
        List<List<Long>> parsedInput = parseInput(input);
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
        return List.of(left, right);
    }

    public static void main(String[] args) {
        Day01 day01 = new Day01();
        String[] input = day01.readInput("src/main/resources/01.txt");
        Long part1Solution = day01.solvePart1(input);
        System.out.println("Part 1: " + part1Solution);
        Long part2Solution = day01.solvePart2(input);
        System.out.println("Part 2: " + part2Solution);
    }
}

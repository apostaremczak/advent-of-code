package pl.apostaremczak.aoc;

import java.util.*;

public class Day19 extends PuzzleSolution {
    Set<String> Towels;
    Map<String, Long> Cache = new HashMap<>();
    Map<String, Long> PossibleArrangementCount = new HashMap<>();

    public Day19(String inputFilename) {
        super(inputFilename);

        this.Towels = new HashSet<>(Arrays.asList(inputLines[0].split(", ")));
        List<String> designs = List.of(Arrays.copyOfRange(inputLines, 2, inputLines.length));

        for (String design : designs) {
            PossibleArrangementCount.put(design, countPossibleArrangements(design));
        }
    }

    public static void main(String[] args) {
        long startTotal = System.currentTimeMillis();
        Day19 day = new Day19("src/main/resources/19.txt");

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

    public Long countPossibleArrangements(String design) {
        if (Cache.containsKey(design)) {
            return Cache.get(design);
        }
        if (design.isEmpty()) {
            return 1L;
        }
        List<String> availableTowels = Towels.stream().filter(design::startsWith).toList();
        if (availableTowels.isEmpty()) {
            Cache.put(design, 0L);
            return 0L;
        }
        Long result = availableTowels.stream().map(towel -> countPossibleArrangements(design.substring(towel.length()))).mapToLong(l -> l).sum();
        Cache.put(design, result);
        return result;
    }

    @Override
    public Long solvePart1() {
        return PossibleArrangementCount.values().stream().filter(v -> v > 0).count();
    }

    @Override
    public Long solvePart2() {
        return PossibleArrangementCount.values().stream().mapToLong(l -> l).sum();
    }
}

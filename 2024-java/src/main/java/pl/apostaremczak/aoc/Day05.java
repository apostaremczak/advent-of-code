package pl.apostaremczak.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day05 extends PuzzleSolution {
    final PrintingManual printingManual;

    public Day05(String inputFilename) {
        super(inputFilename);
        this.printingManual = new PrintingManual(fullRawInput);
    }

    public static Integer getMiddleValue(List<String> line) {
        assert line.size() % 2 == 1;
        String middleValue = line.get((line.size() - 1) / 2);
        return Integer.parseInt(middleValue);
    }

    public static void main(String[] args) {
        long startTotal = System.currentTimeMillis();
        Day05 day = new Day05("src/main/resources/05.txt");

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
        Stream<List<String>> validUpdates = this.printingManual.Updates.stream().filter(printingManual::isValid);
        return sumMiddleValues(validUpdates);
    }

    @Override
    public Long solvePart2() {
        Stream<List<String>> invalidUpdates = this.printingManual.Updates.stream().filter(update -> !printingManual.isValid(update));
        Stream<List<String>> validUpdates = invalidUpdates.map(printingManual::bubbleSort);
        return sumMiddleValues(validUpdates);
    }

    private Long sumMiddleValues(Stream<List<String>> updates) {
        Stream<Integer> middleValues = updates.map(Day05::getMiddleValue);
        return middleValues.mapToLong(i -> i).sum();
    }
}


class PrintingManual {
    final List<String> OrderingRules;
    final List<List<String>> Updates;

    public PrintingManual(String rawManual) {
        String[] rawSplit = rawManual.split("\n\n");
        String rawRules = rawSplit[0];
        String rawUpdates = rawSplit[1];

        this.OrderingRules = Arrays.asList(rawRules.split("\n"));
        this.Updates = Arrays.stream(rawUpdates.split("\n"))
                .map(line -> Arrays.asList(line.split(",")))
                .collect(Collectors.toList());
    }

    public Boolean isValid(List<String> update) {
        for (int i = 0; i < update.size(); i++) {
            String page = update.get(i);
            for (String nextPage : update.subList(i + 1, update.size())) {
                String expectedRule = page + "|" + nextPage;
                if (!OrderingRules.contains(expectedRule)) {
                    return false;
                }
            }
        }

        return true;
    }

    public List<String> bubbleSort(List<String> update) {
        String[] orderedUpdate = new ArrayList<>(update).toArray(String[]::new);
        int updateLength = orderedUpdate.length;
        while (!isValid(List.of(orderedUpdate))) {
            for (int currentIndex = 0; currentIndex < updateLength - 1; currentIndex++) {
                String current = orderedUpdate[currentIndex];
                String next = orderedUpdate[currentIndex + 1];
                if (this.OrderingRules.contains(current + "|" + next)) {
                    // Just to make sure we only keep the legal moves
                    continue;
                } else if (this.OrderingRules.contains(next + "|" + current)) {
                    // Swap places to obey the rules
                    orderedUpdate[currentIndex + 1] = current;
                    orderedUpdate[currentIndex] = next;
                } else {
                    throw new RuntimeException("Somehow no rules were found for " + current + " and " + next);
                }
            }
        }
        return List.of(orderedUpdate);
    }
}

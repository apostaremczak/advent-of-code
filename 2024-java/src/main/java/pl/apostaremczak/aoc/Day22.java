package pl.apostaremczak.aoc;

import pl.apostaremczak.aoc.util.SlidingWindowIterator;

import java.util.*;
import java.util.stream.Collectors;

interface MonkeySecretHelper {
    static Long evolveSecret(Long currentSecret) {
        long bits = 16777216L; // = 2 ^ 24
        long secret = ((currentSecret * 64) ^ currentSecret) % bits;
        secret = ((secret / 32) ^ secret) % bits;
        secret = ((secret * 2048) ^ secret) % bits;
        return secret;
    }
}

public class Day22 extends PuzzleSolution {
    Set<Monkey> Monkeys;

    public Day22(String inputFilename) {
        super(inputFilename);
        Monkeys = Arrays.stream(inputLines)
                .map(Long::parseLong)
                .map(secret -> Monkey.fromSecret(secret, 2000))
                .collect(Collectors.toSet());
    }

    public static void main(String[] args) {
        long startTotal = System.currentTimeMillis();
        Day22 day = new Day22("src/main/resources/22.txt");

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
        return Monkeys.stream()
                .map(Monkey::finalSecret)
                .mapToLong(l -> l)
                .sum();
    }

    @Override
    public Long solvePart2() {
        Map<ChangePattern, Long> profitsPerChangePattern = new HashMap<>();

        for (Monkey monkey : Monkeys) {
            SlidingWindowIterator<Integer> priceWindowIterator = new SlidingWindowIterator<>(monkey.prices(), 5);
            HashSet<ChangePattern> addedFromThisMonkey = new HashSet<>();
            while (priceWindowIterator.hasNext()) {
                List<Integer> prices = priceWindowIterator.next();
                int a = prices.get(0);
                int b = prices.get(1);
                int c = prices.get(2);
                int d = prices.get(3);
                int price = prices.get(4);
                ChangePattern pattern = new ChangePattern(price - d, d - c, c - b, b - a);
                if (!addedFromThisMonkey.contains(pattern)) {
                    profitsPerChangePattern.merge(pattern, (long) price, Long::sum);
                    addedFromThisMonkey.add(pattern);
                }
            }
        }

        return profitsPerChangePattern.values().stream().mapToLong(l -> l).max().orElse(0L);
    }

    private record ChangePattern(int a, int b, int c, int d) {
    }
}

record Monkey(Long initialSecret, Long finalSecret, ArrayList<Integer> prices) {
    public static Monkey fromSecret(Long secret, Integer times) {
        long evolvedSecret = secret;
        ArrayList<Integer> prices = new ArrayList<>();
        prices.add((int) (secret % 10));

        for (int i = 0; i < times; i++) {
            evolvedSecret = MonkeySecretHelper.evolveSecret(evolvedSecret);
            prices.add((int) (evolvedSecret % 10));
        }

        return new Monkey(secret, evolvedSecret, prices);
    }
}

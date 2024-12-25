package pl.apostaremczak.aoc;

import java.util.*;

public class Day25 extends PuzzleSolution {
    public Day25(String inputFilename) {
        super(inputFilename);
    }

    @Override
    public Long solvePart1() {
        Set<FivePinTumblerProduct> keys = new HashSet<>();
        Set<FivePinTumblerProduct> locks = new HashSet<>();
        for (String rawRepresentation : fullRawInput.split("\n\n")) {
            FivePinTumblerProduct product = new FivePinTumblerProduct(rawRepresentation.split("\n"));
            if (product.type.equals(ProductType.KEY)) {
                keys.add(product);
            } else {
                locks.add(product);
            }
        }


        Set<Set<FivePinTumblerProduct>> uniquePairs = new HashSet<>();
        for (FivePinTumblerProduct key: keys) {
            for (FivePinTumblerProduct lock: locks) {
                if (!key.overlapsWith(lock)) {
                    uniquePairs.add(Set.of(key, lock));
                }
            }
        }

        return (long) uniquePairs.size();
    }

    @Override
    public Long solvePart2() {
        return 0L;
    }

    public static void main(String[] args) {
        long startTotal = System.currentTimeMillis();
        Day25 day = new Day25("src/main/resources/25.txt");

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
}

enum ProductType {
    LOCK,
    KEY
}

class FivePinTumblerProduct {
    private final String[] rawRepresentation;
    private final char[][] representation; // Without the detection row at top or bottom
    private final int[] pin;
    ProductType type;

    private ProductType detectType() {
        if (rawRepresentation[0].equals("#####")) {
            return ProductType.LOCK;
        } else {
            return ProductType.KEY;
        }
    }

    private int[] readPin() {
        int[] heights = new int[5];
        for (int i = 0; i < 5; i++) {
            int columnHeight = 0;
            for (char[] row : representation) {
                if (row[i] == '#') {
                    columnHeight += 1;
                }
            }
            heights[i] = columnHeight;
        }
        return heights;
    }

    public FivePinTumblerProduct(String[] rawRepresentation) {
        this.rawRepresentation = rawRepresentation;
        this.type = detectType();
        // Convert String[] rawRepresentation to char[][] representation; skip the first row of raw representation
        String[] representationOnly;
        if (this.type.equals(ProductType.LOCK)) {
            // The locks are schematics that have the top row filled (#) and the bottom row empty
            representationOnly = Arrays.copyOfRange(rawRepresentation, 1, rawRepresentation.length);
        } else {
            // the keys have the top row empty and the bottom row filled
            representationOnly = Arrays.copyOfRange(rawRepresentation, 0, rawRepresentation.length - 1);
        }

        this.representation = new char[6][5];
        for (int i = 0; i < 6; i++) {
            this.representation[i] = representationOnly[i].toCharArray();
        }
        this.pin = readPin();
    }

    public boolean overlapsWith(FivePinTumblerProduct other) {
        assert !this.type.equals(other.type) : "Only products of different types can overlap";
        for (int i = 0; i < 5; i++) {
            if (this.pin[i] + other.pin[i] > 5) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pin[0], pin[1], pin[2], pin[3], pin[4]);
    }
}

package pl.apostaremczak.aoc;

import org.apache.commons.math.linear.*;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

record Arcade(double[] xCoefficients, double[] yCoefficients, double[] prizeLocation) {
    public static Arcade fromInstructions(String instructions) {
        Pattern coeffPattern = Pattern.compile("Button [AB]: X\\+(\\d+), Y+\\+(\\d+)");
        Matcher coeffMatcher = coeffPattern.matcher(instructions);
        double[] xCoeffs = new double[2];
        double[] yCoeffs = new double[2];

        coeffMatcher.find();
        String x1 = coeffMatcher.group(1);
        String y1 = coeffMatcher.group(2);
        xCoeffs[0] = Double.parseDouble(x1);
        yCoeffs[0] = Double.parseDouble(y1);

        coeffMatcher.find();
        String x2 = coeffMatcher.group(1);
        String y2 = coeffMatcher.group(2);
        xCoeffs[1] = Double.parseDouble(x2);
        yCoeffs[1] = Double.parseDouble(y2);

        Pattern prizePattern = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");
        Matcher prizeMatcher = prizePattern.matcher(instructions);
        double[] prizeLocation = new double[2];
        while (prizeMatcher.find()) {
            String x = prizeMatcher.group(1);
            String y = prizeMatcher.group(2);
            prizeLocation[0] = Double.parseDouble(x);
            prizeLocation[1] = Double.parseDouble(y);
        }

        return new Arcade(xCoeffs, yCoeffs, prizeLocation);
    }

    @Override
    public String toString() {
        return "Arcade(x=[" + xCoefficients[0] + ", " + xCoefficients[1] + "], y=[" + yCoefficients[0] + ", " + yCoefficients[1] + "], prize=[" + prizeLocation[0] + ", " + prizeLocation[1] + "])";
    }

    public Long getRequiredTokens() {
        RealMatrix coefficients = new Array2DRowRealMatrix(new double[][]{xCoefficients, yCoefficients});
        RealVector constants = new ArrayRealVector(prizeLocation);
        DecompositionSolver solver = new LUDecompositionImpl(coefficients).getSolver();
        RealVector solution = solver.solve(constants);

        if (solution.isInfinite() || solution.isNaN()) {
            return 0L;
        }
        double[] buttonPushes = solution.getData();

        // Make sure that the results are integers
        long buttonAPushes = Math.round(buttonPushes[0]);
        long buttonBPushes = Math.round(buttonPushes[1]);

        if ((Math.abs((double) buttonAPushes - buttonPushes[0]) < 0.001) && (Math.abs((double) buttonBPushes - buttonPushes[1]) < 0.001)) {
            return 3 * buttonAPushes + buttonBPushes;
        } else {
            return 0L;
        }
    }

    public Arcade affine() {
        double affineX = 10000000000000.0 + prizeLocation[0];
        double affineY = 10000000000000.0 + prizeLocation[1];

        return new Arcade(xCoefficients, yCoefficients, new double[]{affineX, affineY});
    }
}

public class Day13 extends PuzzleSolution {
    public Day13(String inputFilename) {
        super(inputFilename);
    }

    public static void main(String[] args) {
        long startTotal = System.currentTimeMillis();
        Day13 day = new Day13("src/main/resources/13.txt");

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
        return Arrays.stream(fullRawInput.split("\\n\\n"))
                .map(Arcade::fromInstructions)
                .map(Arcade::getRequiredTokens)
                .mapToLong(l -> l)
                .sum();
    }

    @Override
    public Long solvePart2() {
        return Arrays.stream(fullRawInput.split("\\n\\n"))
                .map(Arcade::fromInstructions)
                .map(Arcade::affine)
                .map(Arcade::getRequiredTokens)
                .mapToLong(l -> l)
                .sum();
    }
}

package pl.apostaremczak.aoc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day24 extends PuzzleSolution {
    Map<String, Integer> PuzzleInputValues = new HashMap<>();
    Map<String, Instruction> Instructions = new HashMap<>();

    public Day24(String inputFilename) {
        super(inputFilename);

        // Initial variable values
        Pattern assignmentPattern = Pattern.compile("([a-z\\d]+): ([01])");
        Matcher assignmentMatcher = assignmentPattern.matcher(fullRawInput);
        while (assignmentMatcher.find()) {
            String name = assignmentMatcher.group(1);
            int value = Integer.parseInt(assignmentMatcher.group(2));
            PuzzleInputValues.put(name, value);
        }

        // Gather all instructions
        Pattern instructionPattern = Pattern.compile("([a-z\\d]+) (AND|XOR|OR) ([a-z\\d]+) -> ([a-z\\d]+)");
        Matcher instructionMatcher = instructionPattern.matcher(fullRawInput);
        while (instructionMatcher.find()) {
            String firstOperand = instructionMatcher.group(1);
            String operator = instructionMatcher.group(2);
            String secondOperand = instructionMatcher.group(3);
            String valueHolder = instructionMatcher.group(4);

            Instructions.put(valueHolder, new Instruction(firstOperand, operator, secondOperand, valueHolder));
        }
    }

    public void print(String gateName) {
       System.out.println( print(gateName, 0));
    }

    public String print(String gateName, Integer depth){
        if (gateName.startsWith("x") || gateName.startsWith("y")) {
            return "  ".repeat(depth) + gateName;
        }
        Instruction ins = Instructions.get(gateName);
        return "  ".repeat(depth) + ins.operator() + " (" + gateName + ")\n" +
                print(ins.firstOperand(), depth + 1) + "\n" +
                print(ins.secondOperand(), depth + 1);
    }

    @Override
    public Long solvePart1() {
        BinaryAddition addition = BinaryAddition.from(PuzzleInputValues, Instructions);
        String binaryZ = addition.getBinaryZValue();
        return Long.parseLong(binaryZ, 2);
    }

    public String padGateName(String gateName, Integer number) {
        return gateName + String.format("%02d", number);
    }

    @Override
    public Long solvePart2() {
//        Set<String> incorrectZPositions = new HashSet<>();
//
//        i_position:
//        for (long i = 0; i < 45; i++) {
//            for (long x = (long) Math.pow(2, i); x <= Math.pow(2, i); x++) {
//                long y = 0L;
//                String binaryX = BinaryAddition.toBinary(x);
//                String binaryY = BinaryAddition.toBinary(y);
//                String zResult = BinaryAddition.sumOf(binaryX, binaryY, Instructions);
//                String expectedZ = BinaryAddition.toBinary(x + y);
//
//                if (!zResult.equals(expectedZ)) {
//                    incorrectZPositions.add(padGateName("z", (int) i));
//                    System.out.println("Incorrect result for x = " + x + ", y = " + y + "; i = " + i);
//                    System.out.println("  " + binaryX);
//                    System.out.println("+ " + binaryY);
//                    System.out.println("--------------------------------------------------------");
//                    System.out.println("  " + zResult);
//
//                    System.out.println("Expected:");
//                    System.out.println("  " + expectedZ);
//                    System.out.println("\n\n");
//                    continue i_position;
//                }
//            }
//        }
//
//        System.out.println(incorrectZPositions);

//        for (String incorrectZPosition : incorrectZPositions) {
//            print(incorrectZPosition);
//        }

//        Map<String, Set<String>> potentialSwapInstructions = new HashMap<>();
//
//        // For each potential incorrect z position, trace back all the instructions responsible for this result
//        for (String incorrectZPosition : incorrectZPositions) {
//            Set<String> lineage = traceBackInstructions(incorrectZPosition);
//            potentialSwapInstructions.put(incorrectZPosition, lineage);
//        }
//
//        for (var entry : potentialSwapInstructions.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//            System.out.println("Number of instructions: " + entry.getValue().size());
//        }
        return 0L;
    }

    public Set<String> traceBackInstructions(String gateName) {
        Set<String> instructions = new HashSet<>();
        if (gateName.startsWith("x") || gateName.startsWith("y")) {
            return instructions;
        }
        instructions.add(gateName);
        Instruction instruction = Instructions.get(gateName);
        instructions.addAll(traceBackInstructions(instruction.firstOperand()));
        instructions.addAll(traceBackInstructions(instruction.secondOperand()));
        return instructions;
    }

    public static void main(String[] args) {
        long startTotal = System.currentTimeMillis();
        Day24 day = new Day24("src/main/resources/24.txt");

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


record Instruction(String firstOperand, String operator, String secondOperand, String valueHolder) {
    public void evaluate(Map<String, Integer> values, Map<String, Instruction> instructionMap) {
        if (!values.containsKey(firstOperand)) {
            instructionMap.get(firstOperand).evaluate(values, instructionMap);
        }
        if (!values.containsKey(secondOperand)) {
            instructionMap.get(secondOperand).evaluate(values, instructionMap);
        }

        int firstOperandValue = values.get(firstOperand);
        int secondOperandValue = values.get(secondOperand);
        int result = switch (operator) {
            case "AND" -> firstOperandValue & secondOperandValue;
            case "OR" -> firstOperandValue | secondOperandValue;
            case "XOR" -> firstOperandValue ^ secondOperandValue;
            default -> throw new IllegalArgumentException("Unknown operator " + operator + " in " + this);
        };

        values.put(valueHolder, result);
    }
}

class BinaryAddition {
    Map<String, Integer> Values = new HashMap<>();
    Map<String, Instruction> Instructions = new HashMap<>();
    List<String> ZKeysSortedDesc;

    public static BinaryAddition from(Map<String, Integer> values, Map<String, Instruction> instructions) {
        BinaryAddition addition = new BinaryAddition();
        addition.Values = new HashMap<>(values);
        addition.Instructions = new HashMap<>(instructions);
        addition.ZKeysSortedDesc = instructions.keySet().stream()
                .filter(k -> k.startsWith("z"))
                .sorted(Collections.reverseOrder())
                .toList();

        return addition;
    }

    public static String sumOf(String binaryX, String binaryY, Map<String, Instruction> instructions) {
        Map<String, Integer> testValues = new HashMap<>();
        for (int i = 0; i < binaryX.length(); i++) {
            testValues.put(String.format("x%02d", i), Character.getNumericValue(binaryX.charAt(binaryX.length() - 1 - i)));
        }
        for (int i = 0; i < binaryY.length(); i++) {
            testValues.put(String.format("y%02d", i), Character.getNumericValue(binaryY.charAt(binaryY.length() - 1 - i)));
        }

        return BinaryAddition.from(testValues, instructions).getBinaryZValue();
    }

    private void evaluateAll() {
        // Evaluate instructions
        for (Instruction instruction : Instructions.values()) {
            instruction.evaluate(Values, Instructions);
        }
    }

    // Indices: 00 -> 45, with trailing zeros if needed
    public String getBinaryZValue() {
        evaluateAll();
        List<String> zValues = ZKeysSortedDesc.stream().map(Values::get).map(String::valueOf).toList();
        StringBuilder resultBinary = new StringBuilder();
        for (String zValue : zValues) {
            resultBinary.append(zValue);
        }
        return resultBinary.toString();
    }

    public static String toBinary(Long i) {
        // Add trailing zeroes if needed
        return String.format("%46s", Long.toBinaryString(i)).replace(' ', '0');
    }
}

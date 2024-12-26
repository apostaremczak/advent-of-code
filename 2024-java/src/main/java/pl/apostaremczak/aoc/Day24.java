package pl.apostaremczak.aoc;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day24 extends PuzzleSolution {
    Map<String, Integer> PuzzleInputValues = new HashMap<>();
    Map<String, Instruction> Instructions = new HashMap<>();
    // "x00 XOR y00" -> z00, "y00 XOR x00" -> z00
    // Interchangeable for easier lookups
    Map<String, String> InstructionToResult = new HashMap<>();

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
            InstructionToResult.put(firstOperand + " " + operator + " " + secondOperand, valueHolder);
            InstructionToResult.put(secondOperand + " " + operator + " " + firstOperand, valueHolder);
        }
    }

    @Override
    public Long solvePart1() {
        BinaryAddition addition = BinaryAddition.from(PuzzleInputValues, Instructions);
        String binaryZ = addition.getBinaryZValue();
        return Long.parseLong(binaryZ, 2);
    }

    @Override
    public Long solvePart2() {
        String carryOn = "ksw"; // First carry over was found manually
        Set<String> mermaidGraph = new HashSet<>();

        // Iteratively go through the instructions and verify that they match the construction of a full adder
        for (int i = 1; i < 44; i++) {
            Set<String> toBeSwapped = new HashSet<>();
            carryOn = validate(i, carryOn, toBeSwapped);
            if (!toBeSwapped.isEmpty()) {
                // If something breaks the flow, print it to a Mermaid graph to be fixed manually
                for (String swapGate : toBeSwapped) {
                    mermaidGraph.addAll(toMermaid(swapGate));
                }
                mermaidGraph.addAll(toMermaid(padGateName("z", i)));
                writeToMermaidFile(mermaidGraph);
                throw new IllegalArgumentException("Found something to be swapped at i = " + i + ": " + toBeSwapped);
            }
        }

        // Fixed iteratively
        String swapped = String.join(",", Arrays.stream("z15,fph,gds,z21,wrk,jrs,z34,cqk".split(",")).sorted().toList());
        System.out.println(swapped);
        return 0L;
    }

    public String padGateName(String gateName, Integer number) {
        return gateName + String.format("%02d", number);
    }

    /**
     * c_i = carry over, input for bit i, calculated in the previous iteration
     * c_{i+1} = (x_i AND y_i) OR (c_i AND (x_i XOR y_i))
     * z_i = sum output at bit i
     * z_i = (x_i XOR y_i) XOR c_i
     * Returns the carryOver to be passed for the next iteration
     */
    public String validate(int i, String carryOver, Set<String> gatesToBeSwapped) {
        String x_i = padGateName("x", i);
        String y_i = padGateName("y", i);
        String sumOutput = padGateName("z", i);

        // x_xor_y = x_i XOR y_i = y_i XOR x_1
        String xXorY = InstructionToResult.get(x_i + " XOR " + y_i);
        // x_i XOR y_i XOR carryOver -> sumOutput
        // if not, then sumOutput must be switched with whatever xXorY XOR carryOver points to
        String sumOutputTarget = InstructionToResult.get(xXorY + " XOR " + carryOver);
        if (sumOutputTarget == null) {
            gatesToBeSwapped.add(xXorY);
            gatesToBeSwapped.add(carryOver);
            return null;
        }
        if (!sumOutputTarget.equals(sumOutput)) {
            gatesToBeSwapped.add(sumOutput);
            gatesToBeSwapped.add(sumOutputTarget);
            return null;
        }
        // xAndY = x_i AND y_i
        String xAndY = InstructionToResult.get(x_i + " AND " + y_i);
        // It has to be an intermediate variable; if it points to z, then we must swap with something else later on
        if (xAndY.startsWith("z")) {
            gatesToBeSwapped.add(xAndY);
            return null;
        }
        // xXorYAndCarryOver = (x_i XOR y_i) AND c_i
        String xXorYAndCarryOver = InstructionToResult.get(xXorY + " AND " + carryOver);
        // It has to be an intermediate variable; if it points to z, then we must swap with something else later on
        if (xXorYAndCarryOver.startsWith("z")) {
            gatesToBeSwapped.add(xXorYAndCarryOver);
            return null;
        }

        // next carry on = xAndY OR xXorYAndCarryOver
        String nextCarryOver = InstructionToResult.get(xAndY + " OR " + xXorYAndCarryOver);
        if (nextCarryOver == null) {
            throw new RuntimeException("i = " + i + ", xAndY = " + xAndY + ", xXorYAndCarryOver = " + xXorYAndCarryOver);
        }
        // It has to be an intermediate variable; if it points to z, then we must swap with something else later on
        if (nextCarryOver.startsWith("z")) {
            gatesToBeSwapped.add(nextCarryOver);
            return null;
        }

        return nextCarryOver;
    }

    // Turn all instructions that proceed gateName
    public Set<String> toMermaid(String gateName, Set<String> lines) {
        if (gateName.startsWith("x") || gateName.startsWith("y")) {
            return lines;
        }
        Instruction ins = Instructions.get(gateName);
        String modifiedOperator = ins.operator() + "_" + ins.valueHolder();
        lines.add(ins.firstOperand() + " --> " + modifiedOperator + "[\"" + ins.operator() + "\"]");
        lines.add(ins.secondOperand() + " --> " + modifiedOperator + "[\"" + ins.operator() + "\"]");
        lines.add(modifiedOperator + " --> " + ins.valueHolder());
        toMermaid(ins.firstOperand(), lines);
        toMermaid(ins.secondOperand(), lines);

        return lines;
    }

    public Set<String> toMermaid(String gateName) {
        return toMermaid(gateName, new HashSet<>());
    }

    public void writeToMermaidFile(Set<String> graph) {
        try (PrintWriter writer = new PrintWriter("mermaid.md")) {
            writer.println("```mermaid\ngraph TD;");
            for (String line : graph) {
                writer.println(line);
            }
            writer.println("\n```");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        long startTotal = System.currentTimeMillis();
        Day24 day = new Day24("src/main/resources/24_4.txt");

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
}

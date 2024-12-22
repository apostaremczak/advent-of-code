package pl.apostaremczak.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day17 extends PuzzleSolution {
    Computer computer;

    public Day17(String inputFilename) {
        super(inputFilename);
        this.computer = Computer.fromInput(fullRawInput);
    }

    public static void main(String[] args) {
        long startTotal = System.currentTimeMillis();
        Day17 day = new Day17("src/main/resources/17.txt");

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
        computer.run();
        System.out.println("Part 1: " + computer.getPrintableOutput());

        return 0L;
    }

    @Override
    public Long solvePart2() {
        // A **quine** is a computer program that takes no input and produces a copy of its own source code as its only output.
        return findRegisterA(0, "").orElse(0L);
    }

    public String getOutputForRegisterA(Long registerAValue) {
        Computer computer = Computer.fromInput(fullRawInput);
        computer.RegisterA = registerAValue;
        computer.run();
        return computer.getPrintableOutput();
    }

    // Great explanation of binary operations by HyperNeutrino! https://www.youtube.com/watch?v=y-UPxMAh2N8
    public OptionalLong findRegisterA(Integer index, String currentAnswerInBinary) {
        if (index != 0) {
            long currentRegisterA = Long.parseLong(currentAnswerInBinary, 2);
            if (getOutputForRegisterA(currentRegisterA).equals(computer.printInput())) {
                return OptionalLong.of(currentRegisterA);
            }
            if (index == computer.Inputs.size()) {
                return OptionalLong.empty();
            }
        }

        // Search for the next 3 bits of the answer
        long searchRangeStart = index == 0 ? 0 : Long.parseLong(currentAnswerInBinary + "000", 2);
        long searchRangeEnd = index == 0 ? 3 : Long.parseLong(currentAnswerInBinary + "111", 2);
        String expectedOutput = String.join(",", computer.Inputs.reversed().subList(0, index + 1).reversed().stream().map(String::valueOf).toList());
        for (long a = searchRangeStart; a <= searchRangeEnd; a++) {
            String searchOutput = getOutputForRegisterA(a);
            if (searchOutput.equals(expectedOutput)) {
                OptionalLong nextSearch = findRegisterA(index + 1, Long.toBinaryString(a));
                if (nextSearch.isEmpty()) {
                    continue;
                }
                return nextSearch;
            }
        }

        return OptionalLong.empty();
    }
}

class Computer {
    Long RegisterA = 0L;
    Long RegisterB = 0L;
    Long RegisterC = 0L;
    Integer InstructionPointer = 0;
    List<Long> Outputs = new ArrayList<>();
    List<Integer> Inputs;

    public static Computer fromInput(String fullRawInput) {
        Computer computer = new Computer();

        Pattern computerPattern = Pattern.compile("""
                Register A: (\\d+)
                Register B: (\\d+)
                Register C: (\\d+)
                
                Program: ([\\d+,]+)
                """);
        Matcher matcher = computerPattern.matcher(fullRawInput);
        if (matcher.find()) {
            computer.RegisterA = Long.parseLong(matcher.group(1));
            computer.RegisterB = Long.parseLong(matcher.group(2));
            computer.RegisterC = Long.parseLong(matcher.group(3));
            computer.Inputs = Arrays.stream(matcher.group(4).split(",")).map(Integer::parseInt).toList();
        } else {
            throw new IllegalArgumentException("Failed to parse computer instructions");
        }
        return computer;
    }

    public Long comboOperand(Integer opcode) {
        return switch (opcode) {
            case 0 -> 0L;
            case 1 -> 1L;
            case 2 -> 2L;
            case 3 -> 3L;
            case 4 -> RegisterA;
            case 5 -> RegisterB;
            case 6 -> RegisterC;
            case 7 -> throw new IllegalArgumentException("Tried to pass 7 as a combo operand");
            default -> throw new IllegalArgumentException("Weird opcode passed: " + opcode);
        };
    }

    public void run() {
        while (InstructionPointer < Inputs.size()) {
            Integer opcode = Inputs.get(InstructionPointer);
            Integer operand = Inputs.get(InstructionPointer + 1);
            switch (opcode) {
                case 0:
                    // Division can be replaced by bit shifting
                    // a // 2^x == a >> x
                    RegisterA = RegisterA >> comboOperand(operand);
                    InstructionPointer += 2;
                    break;
                case 1:
                    RegisterB = RegisterB ^ operand;
                    InstructionPointer += 2;
                    break;
                case 2:
                    RegisterB = comboOperand(operand) % 8;
                    InstructionPointer += 2;
                    break;
                case 3:
                    if (RegisterA != 0) {
                        InstructionPointer = operand;
                    } else {
                        InstructionPointer += 2;
                    }
                    break;
                case 4:
                    RegisterB = RegisterB ^ RegisterC;
                    InstructionPointer += 2;
                    break;
                case 5:
                    Outputs.add(comboOperand(operand) % 8);
                    InstructionPointer += 2;
                    break;
                case 6:
                    RegisterB = RegisterA >> comboOperand(operand);
                    InstructionPointer += 2;
                    break;
                case 7:
                    RegisterC = RegisterA >> comboOperand(operand);
                    InstructionPointer += 2;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown opcode: " + opcode);
            }
        }
    }

    public String getPrintableOutput() {
        List<String> outputStrings = Outputs.stream().map(String::valueOf).toList();
        return String.join(",", outputStrings);
    }

    public String printInput() {
        List<String> inputStrings = Inputs.stream().map(String::valueOf).toList();
        return String.join(",", inputStrings);
    }
}

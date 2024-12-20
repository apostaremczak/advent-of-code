package pl.apostaremczak.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day17 extends PuzzleSolution {
    public Day17(String inputFilename) {
        super(inputFilename);
    }

    @Override
    public Long solvePart1() {
        Computer computer = Computer.fromInput(fullRawInput);
        computer.run();
        System.out.println(computer.printOutput());
        return 0L;
    }

    @Override
    public Long solvePart2() {
        for (int registerA = 100_000_000; registerA < 1_000_000_000; registerA++) {
            assert registerA < Integer.MAX_VALUE;
            Computer computer = Computer.fromInput(fullRawInput);
            computer.RegisterA = registerA;
            computer.run();
            if (computer.printInput().equals(computer.printOutput())) {
                return (long) registerA;
            }
        }
        return 0L;
    }

    public static void main(String[] args) {
        Day17 day17 = new Day17("src/main/resources/17.txt");
        Long part1Solution = day17.solvePart1();
        System.out.println("Part 1: " + part1Solution);
        Long part2Solution = day17.solvePart2();
        System.out.println("Part 2: " + part2Solution);
    }
}

class Computer {
    Integer RegisterA = 0;
    Integer RegisterB = 0;
    Integer RegisterC = 0;
    Integer InstructionPointer = 0;
    List<Integer> Outputs = new ArrayList<>();
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
            computer.RegisterA = Integer.parseInt(matcher.group(1));
            computer.RegisterB = Integer.parseInt(matcher.group(2));
            computer.RegisterC = Integer.parseInt(matcher.group(3));
            computer.Inputs = Arrays.stream(matcher.group(4).split(",")).map(Integer::parseInt).toList();
        } else {
            throw new IllegalArgumentException("Failed to parse computer instructions");
        }
        return computer;
    }

    public Integer comboOperand(Integer opcode) {
        return switch (opcode) {
            case 0 -> 0;
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 3;
            case 4 -> RegisterA;
            case 5 -> RegisterB;
            case 6 -> RegisterC;
            case 7 -> throw new IllegalArgumentException("Tried to pass 7 as a combo operand");
            default -> throw new IllegalArgumentException("Weird opcode passed: " + opcode);
        };
    }

    public void run() {
        while (InstructionPointer < Inputs.size()) {
            if (Outputs.size() > Inputs.size()) {
                break;
            }
            Integer opcode = Inputs.get(InstructionPointer);
            Integer operand = Inputs.get(InstructionPointer + 1);
            switch (opcode) {
                case 0:
                    RegisterA = (int) (RegisterA / Math.pow(2, comboOperand(operand)));
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
                    RegisterB = (int) (RegisterA / Math.pow(2, comboOperand(operand)));
                    InstructionPointer += 2;
                    break;
                case 7:
                    RegisterC = (int) (RegisterA / Math.pow(2, comboOperand(operand)));
                    InstructionPointer += 2;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown opcode: " + opcode);
            }
        }
    }

    public String printOutput() {
        List<String> outputStrings = Outputs.stream().map(String::valueOf).toList();
        return String.join(",", outputStrings);
    }

    public String printInput() {
        List<String> inputStrings = Inputs.stream().map(String::valueOf).toList();
        return String.join(",", inputStrings);
    }
}

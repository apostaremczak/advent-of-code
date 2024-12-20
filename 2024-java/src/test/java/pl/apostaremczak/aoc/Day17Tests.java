package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day17Tests {
    private final Day17 day17 = new Day17("src/test/resources/17.txt");

    @Test
    public void testSolvePart1() {
        Computer computer = Computer.fromInput(day17.fullRawInput);
        computer.run();
        String result = computer.printOutput();
        assertEquals("4,6,3,5,6,3,5,2,1,0", result);
    }

    @Test
    public void testSolvePart2() {
        Day17 day = new Day17("src/test/resources/17_2.txt");
        Long result = day.solvePart2();
        assertEquals(117440L, result);
    }

    @Test
    public void testComputer1() {
        Computer computer = new Computer();
        computer.RegisterC = 9;
        computer.Inputs = List.of(2, 6);
        computer.run();

        assertEquals(1, computer.RegisterB);
    }

    @Test
    public void testComputer2() {
        Computer computer = new Computer();
        computer.RegisterA = 10;
        computer.Inputs = List.of(5,0,5,1,5,4);
        computer.run();

        assertEquals("0,1,2", computer.printOutput());
    }

    @Test
    public void testComputer3() {
        Computer computer = new Computer();
        computer.RegisterA = 2024;
        computer.Inputs = List.of(0,1,5,4,3,0);
        computer.run();

        assertEquals("4,2,5,6,7,7,7,7,3,1,0", computer.printOutput());
        assertEquals(0, computer.RegisterA);
    }
}

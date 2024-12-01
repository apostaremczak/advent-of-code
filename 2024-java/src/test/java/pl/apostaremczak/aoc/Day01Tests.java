package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day01Tests {
    private final Day01 day01 = new Day01();

    @Test
    public void testSolvePart1() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("src/test/resources/01.txt"));
        String[] input = lines.toArray(new String[0]);
        Long result = day01.solvePart1(input);
        assertEquals(11L, result);
    }

    @Test
    public void testSolvePart2() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("src/test/resources/01.txt"));
        String[] input = lines.toArray(new String[0]);
        Long result = day01.solvePart2(input);
        assertEquals(31L, result);
    }
}

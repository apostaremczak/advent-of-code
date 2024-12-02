package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day02Tests {
    private final Day02 day02 = new Day02();

    @Test
    public void testSolvePart1() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("src/test/resources/02.txt"));
        String[] input = lines.toArray(new String[0]);
        Long result = day02.solvePart1(input);
        assertEquals(2L, result);
    }

    @Test
    public void testSolvePart2() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("src/test/resources/02.txt"));
        String[] input = lines.toArray(new String[0]);
        Long result = day02.solvePart2(input);
        assertEquals(4L, result);
    }
}

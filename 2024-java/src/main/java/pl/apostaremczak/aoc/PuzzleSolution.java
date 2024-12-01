package pl.apostaremczak.aoc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public interface PuzzleSolution {
    /**
     * Reads lines from the given input txt file and split it into lines.
     */
    default String[] readInput(String filename) {
        List<String> input = new ArrayList<>();
        try {
            input = Files.readAllLines(new File(filename).toPath());
        } catch (IOException e) {
            e.fillInStackTrace();
        }

        return input.toArray(new String[0]);
    }

    Long solvePart1(String[] input);

    Long solvePart2(String[] input);
}

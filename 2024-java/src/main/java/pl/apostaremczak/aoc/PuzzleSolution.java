package pl.apostaremczak.aoc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public abstract class PuzzleSolution {
    protected String inputFilename;
    protected String[] inputLines;

    public PuzzleSolution(String inputFilename) {
        this.inputFilename = inputFilename;
        this.inputLines = readInput(inputFilename);
    }

    private String[] readInput(String filename) {
        List<String> input = new ArrayList<>();
        try {
            input = Files.readAllLines(new File(filename).toPath());
        } catch (IOException e) {
            e.fillInStackTrace();
        }

        return input.toArray(new String[0]);
    }

    abstract Long solvePart1();

    abstract Long solvePart2();
}

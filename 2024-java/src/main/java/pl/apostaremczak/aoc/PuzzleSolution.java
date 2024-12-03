package pl.apostaremczak.aoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public abstract class PuzzleSolution {
    protected String inputFilename;
    protected String[] inputLines;
    protected String fullRawInput;

    public PuzzleSolution(String inputFilename) {
        this.inputFilename = inputFilename;
        this.inputLines = readInputLines(inputFilename);
        this.fullRawInput = readFullInput(inputFilename);
    }

    abstract Long solvePart1();

    abstract Long solvePart2();

    private String[] readInputLines(String filename) {
        List<String> input = new ArrayList<>();
        try {
            input = Files.readAllLines(new File(filename).toPath());
        } catch (IOException e) {
            e.fillInStackTrace();
        }

        return input.toArray(new String[0]);
    }

    private String readFullInput(String filename) {
        File file = new File(filename);
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        byte[] data = new byte[(int) file.length()];
        try {
            fis.read(data);
            fis.close();
            return new String(data, StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

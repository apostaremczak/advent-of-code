package pl.apostaremczak.aoc.util;

import java.util.*;

public class Map2D<T> {
    public final T[][] internal;
    public final Integer MAX_ROW_INDEX;
    public final Integer MAX_COLUMN_INDEX;

    public Map2D(T[][] lines) {
        this.internal = lines;
        this.MAX_ROW_INDEX = lines.length - 1;
        if (lines.length == 0) {
            this.MAX_COLUMN_INDEX = 0;
        } else {
            this.MAX_COLUMN_INDEX = lines[0].length - 1;
        }
    }

    public static Map2D<Character> fromStringInputLines(String[] inputLines) {
        return new Map2D<>(Arrays.stream(inputLines)
                .map(line -> line.chars().mapToObj(c -> (char) c).toArray(Character[]::new))
                .toArray(Character[][]::new));
    }

    public Optional<T> safeGetAt(Coord2D coord) {
        if (!isWithinBounds(coord)) {
            return Optional.empty();
        }
        return Optional.of(internal[coord.row()][coord.column()]);
    }

    public T getAt(Coord2D coord) {
        assert isWithinBounds(coord) : "Requested a field of out the map's boundaries";
        return internal[coord.row()][coord.column()];
    }

    public boolean isWithinBounds(Coord2D coord) {
        return coord.row() >= 0 && coord.row() <= MAX_ROW_INDEX && coord.column() >= 0 && coord.column() <= MAX_COLUMN_INDEX;
    }

    public Optional<Coord2D> findFirst(T element) {
        for (int rowIdx = 0; rowIdx <= MAX_ROW_INDEX; rowIdx++) {
            for (int colIdx = 0; colIdx <= MAX_COLUMN_INDEX; colIdx++) {
                Coord2D currentPosition = new Coord2D(rowIdx, colIdx);
                if (getAt(currentPosition).equals(element)) {
                    return Optional.of(currentPosition);
                }
            }
        }
        return Optional.empty();
    }

    public Set<Coord2D> findAll(T element) {
        Set<Coord2D> occurrences = new HashSet<>();
        for (int rowIdx = 0; rowIdx <= MAX_ROW_INDEX; rowIdx++) {
            for (int colIdx = 0; colIdx <= MAX_COLUMN_INDEX; colIdx++) {
                Coord2D currentPosition = new Coord2D(rowIdx, colIdx);
                if (getAt(currentPosition).equals(element)) {
                    occurrences.add(currentPosition);
                }
            }
        }
        return occurrences;
    }

    public Iterator<Coord2D> positionIterator() {
        return new Iterator<Coord2D>() {
            private int currentRow = 0;
            private int currentCol = 0;

            @Override
            public boolean hasNext() {
                return currentRow <= MAX_ROW_INDEX && currentCol <= MAX_COLUMN_INDEX;
            }

            @Override
            public Coord2D next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Coord2D current = new Coord2D(currentRow, currentCol);
                if (currentCol < MAX_COLUMN_INDEX) {
                    currentCol++;
                } else {
                    currentCol = 0;
                    currentRow++;
                }
                return current;
            }
        };
    }

    /**
     * All points in the grid that are at == radius distance from the center, calculated with the Manhattan metric.
     */
    public Set<Coord2D> getManhattanDiscPoints(Coord2D center, Integer radius) {
        Set<Coord2D> discElements = new HashSet<>();
        for (Iterator<Coord2D> it = this.positionIterator(); it.hasNext(); ) {
            Coord2D gridPoint = it.next();
            if (!gridPoint.equals(center) && center.manhattanDistanceFrom(gridPoint).equals(radius)) {
                discElements.add(gridPoint);
            }
        }
        return discElements;
    }

    /**
     * All points in the grid that are at <= radius distance from the center, calculated with the Manhattan metric.
     */
    public Set<Coord2D> getManhattanClosedBallPoints(Coord2D center, Integer radius) {
        Set<Coord2D> discElements = new HashSet<>();
        for (Iterator<Coord2D> it = this.positionIterator(); it.hasNext(); ) {
            Coord2D gridPoint = it.next();
            if (!gridPoint.equals(center) && center.manhattanDistanceFrom(gridPoint) <= radius) {
                discElements.add(gridPoint);
            }
        }
        return discElements;
    }
}

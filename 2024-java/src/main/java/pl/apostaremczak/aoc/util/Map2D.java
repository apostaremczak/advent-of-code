package pl.apostaremczak.aoc.util;

import java.util.Arrays;
import java.util.Optional;

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

    public Optional<T> safeGetAt(Coord2D coord) {
        if (!isWithinBounds(coord)) {
            return Optional.empty();
        }
        return Optional.of(internal[coord.row()][coord.column()]);
    }

    public T getAt(Coord2D coord) {
        assert isWithinBounds(coord): "Requested a field of out the map's boundaries";
        return internal[coord.row()][coord.column()];
    }

    public boolean isWithinBounds(Coord2D coord) {
        return coord.row() >= 0 && coord.row() <= MAX_ROW_INDEX && coord.column() >= 0 && coord.column() <= MAX_COLUMN_INDEX;
    }

    public static Map2D<Character> fromStringInputLines(String[] inputLines) {
        return new Map2D<>(Arrays.stream(inputLines)
                .map(line -> line.chars().mapToObj(c -> (char) c).toArray(Character[]::new))
                .toArray(Character[][]::new));
    }
}

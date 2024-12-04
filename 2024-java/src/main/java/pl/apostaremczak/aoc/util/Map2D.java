package pl.apostaremczak.aoc.util;

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

    public Optional<T> getAt(Coord2D coord) {
        if (coord.row < 0 || coord.column < 0 || coord.row > MAX_ROW_INDEX || coord.column > MAX_COLUMN_INDEX) {
            return Optional.empty();
        }
        return Optional.of(internal[coord.row][coord.column]);
    }
}

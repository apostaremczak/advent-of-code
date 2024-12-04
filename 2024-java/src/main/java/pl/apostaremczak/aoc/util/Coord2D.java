package pl.apostaremczak.aoc.util;

import java.util.ArrayList;
import java.util.List;

public class Coord2D implements CoordinateHelpers {
    final public Integer row;
    final public Integer column;

    public Coord2D(Integer row, Integer column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public String toString() {
        return "Coord2d(row=" + row + ", column=" + column + ")";
    }

    public Coord2D add(Coord2D otherCoord) {
        return new Coord2D(this.row + otherCoord.row, this.column + otherCoord.column);
    }

    public Coord2D scalarMultiplication(Integer times) {
        return new Coord2D(times * this.row, times * this.column);
    }

    public List<Coord2D> getAllSurrounding() {
        List<Coord2D> surrounding = new ArrayList<>();
        for (Coord2D direction : Directions2D) {
            surrounding.add(this.add(direction));
        }
        return surrounding;
    }
}

package pl.apostaremczak.aoc.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record Coord2D(Integer row, Integer column) implements DirectionSupport {

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

    public Coord2D minus(Coord2D otherCord) {
        return new Coord2D(this.row - otherCord.row, this.column - otherCord.column);
    }

    /**
     * All nodes around this coordinate point, including points on diagonal
     */
    public List<Coord2D> getAllSurrounding() {
        List<Coord2D> surrounding = new ArrayList<>();
        for (Coord2D direction : Directions2D) {
            surrounding.add(this.add(direction));
        }
        return surrounding;
    }

    public Set<Coord2D> getStraightSurrounding() {
        Set<Coord2D> surrounding = new HashSet<>();
        for (Coord2D direction : StraightDirections2D) {
            surrounding.add(this.add(direction));
        }
        return surrounding;
    }

    /**
     * @param angle (In degrees) Angle of rotation, must be a multiple of 90. Anticlockwise.
     * @return Rotated coordinates.
     */
    public Coord2D rotateRightAngle(double angle) {
        assert angle % 90 == 0 : "Rotation must be a multiple of 90, got " + angle;
        double angleRadians = Math.toRadians(angle);
        double newX = this.row * Math.cos(angleRadians) - this.column * Math.sin(angleRadians);
        double newY = this.row * Math.sin(angleRadians) + this.column * Math.cos(angleRadians);
        // Make sure the result coordinates are integers
        int intNewX = (int) newX;
        int intNewY = (int) newY;
        assert ((Math.abs(intNewX - newX) < 0.00001) && (Math.abs(intNewY - newY) < 0.00001)) : "Rotation result produced non-integer coordinates";

        return new Coord2D((int) newX, (int) newY);
    }

    public Double euclideanDistance(Coord2D other) {
        return Math.sqrt(Math.pow(this.row - other.row, 2) + Math.pow(this.column - other.column, 2));
    }
}

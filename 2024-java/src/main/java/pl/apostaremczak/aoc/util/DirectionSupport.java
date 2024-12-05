package pl.apostaremczak.aoc.util;

public interface DirectionSupport {
    Coord2D TOP = new Coord2D(-1, 0);
    Coord2D TOP_RIGHT = new Coord2D(-1, 1);
    Coord2D RIGHT = new Coord2D(0, 1);
    Coord2D BOTTOM_RIGHT = new Coord2D(1, 1);
    Coord2D BOTTOM = new Coord2D(1, 0);
    Coord2D BOTTOM_LEFT = new Coord2D(1, -1);
    Coord2D LEFT = new Coord2D(0, -1);
    Coord2D TOP_LEFT = new Coord2D(-1, -1);

    Coord2D[] Directions2D = {
            TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, TOP_LEFT
    };
}

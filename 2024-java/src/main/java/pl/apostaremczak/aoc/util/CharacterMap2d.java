package pl.apostaremczak.aoc.util;

import java.util.*;

public class CharacterMap2d extends Map2D<Character> {
    Character WallSymbol;

    public CharacterMap2d(Character[][] lines, Character wallSymbol) {
        super(lines);
        WallSymbol = wallSymbol;
    }

    public static CharacterMap2d fromStringInputLines(String[] inputLines, Character wallSymbol) {
        return new CharacterMap2d(Arrays.stream(inputLines)
                .map(line -> line.chars().mapToObj(c -> (char) c).toArray(Character[]::new))
                .toArray(Character[][]::new), wallSymbol);
    }

    public Boolean isWall(Coord2D position) {
        return this.safeGetAt(position).orElse('£').equals(WallSymbol);
    }

    /**
     * BFS to find maze distances between a start point and all the other points.
     *
     * @param startPoint From where should the distances be calculated?
     * @return Map: { point -> distance from start to that point }
     */
    public Map<Coord2D, Long> getDistancesFrom(Coord2D startPoint) {
        ArrayDeque<Coord2D> queue = new ArrayDeque<>();
        Map<Coord2D, Long> distancesFromStart = new HashMap<>();

        queue.offer(startPoint);
        distancesFromStart.put(startPoint, 0L);

        while (!queue.isEmpty()) {
            Coord2D current = queue.remove();
            Long currentDistance = distancesFromStart.get(current);
            for (Coord2D neighbor : current.getStraightSurrounding()) {
                // Hasn't been visited yet, is still on the map and isn't a wall
                if (!distancesFromStart.containsKey(neighbor) && isWithinBounds(neighbor) && !safeGetAt(neighbor).orElse('_').equals(WallSymbol)) {
                    distancesFromStart.put(neighbor, currentDistance + 1);
                    queue.offer(neighbor);
                }
            }
        }
        return distancesFromStart;
    }
}

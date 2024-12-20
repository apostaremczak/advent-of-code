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

    /**
     * BFS for finding distances from the start point to all the points on the map
     */
    public Map<DirectedMovement, Long> getDirectedDistancesFrom(DirectedMovement start, Integer directionChangePenalty) {
        PriorityQueue<State> queue = new PriorityQueue<>();
        Map<DirectedMovement, Long> distancesFromStart = new HashMap<>();

        queue.offer(new State(start, 0L));
        distancesFromStart.put(start, 0L);

        while (!queue.isEmpty()) {
            State state = queue.remove();
            Coord2D currentNode = state.move.position();
            Coord2D currentDirection = state.move.direction();
            long distanceFromStart = state.distanceFromStart;
            DirectedMovement move = new DirectedMovement(currentNode, currentDirection);
            if (distancesFromStart.getOrDefault(move, Long.MAX_VALUE) < distanceFromStart) {
                continue;
            }
            Coord2D[] potentialDirections = {currentDirection, currentDirection.rotateRightAngle(90), currentDirection.rotateRightAngle(-90)};
            int[] directionEdgeWeight = {1, 1 + directionChangePenalty, 1 + directionChangePenalty};
            for (int i = 0; i < 3; i++) {
                Coord2D nextDirection = potentialDirections[i];
                Coord2D nextNode = currentNode.add(nextDirection);
                DirectedMovement nextMove = new DirectedMovement(nextNode, nextDirection);
                long nextNodeDistance = distanceFromStart + directionEdgeWeight[i];
                // Only explore potentially shorter paths
                if (this.isWithinBounds(nextNode) && !this.isWall(nextNode) && distancesFromStart.getOrDefault(nextMove, Long.MAX_VALUE) > nextNodeDistance) {
                    queue.offer(new State(nextMove, nextNodeDistance));
                    distancesFromStart.put(nextMove, nextNodeDistance);
                }
            }
        }

        return distancesFromStart;
    }

    private record State(DirectedMovement move, Long distanceFromStart) implements Comparable<State> {
        @Override
        public int compareTo(State o) {
            return Long.compare(this.distanceFromStart, o.distanceFromStart);
        }
    }
}


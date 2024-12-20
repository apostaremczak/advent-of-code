package pl.apostaremczak.aoc;

import pl.apostaremczak.aoc.util.Coord2D;
import pl.apostaremczak.aoc.util.DirectionSupport;
import pl.apostaremczak.aoc.util.Map2D;

import java.util.*;

public class Day16 extends PuzzleSolution {
    public Day16(String inputFilename) {
        super(inputFilename);
    }

    @Override
    public Long solvePart1() {
        ReindeerRaceMaze maze = new ReindeerRaceMaze(inputLines);
        return maze.getBestPathScore();
    }

    @Override
    public Long solvePart2() {
        return 0L;
    }

    public static void main(String[] args) {
        Day16 day16 = new Day16("src/main/resources/16.txt");
        Long part1Solution = day16.solvePart1();
        System.out.println("Part 1: " + part1Solution);
        Long part2Solution = day16.solvePart2();
        System.out.println("Part 2: " + part2Solution);
    }
}

class MazeTile implements Comparable<MazeTile>, DirectionSupport {
    private Coord2D position;

    private LinkedList<StackEntry> shortestPath = new LinkedList<>();

    public final Set<MazeTile> allPotentialPathEntries = new HashSet<>();

    private Integer distance = Integer.MAX_VALUE;

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer newDistance) {
        this.distance = newDistance;
    }

    public Coord2D getPosition() {
        return this.position;
    }

    public LinkedList<StackEntry> getShortestPath() {
        return this.shortestPath;
    }

    public void setShortestPath(LinkedList<StackEntry> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public static MazeTile fromPosition(Coord2D tilePosition) {
        MazeTile tile = new MazeTile();
        tile.position = tilePosition;
        return tile;
    }

    @Override
    public int compareTo(MazeTile o) {
        return Integer.compare(this.getDistance(), o.getDistance());
    }

    @Override
    public String toString() {
        return "MazeTile{" +
                "position=" + position +
                ", distance=" + distance +
                '}';
    }
}

class ReindeerRaceMaze implements DirectionSupport {
    private final Coord2D StartTilePosition;
    private final Coord2D EndTilePosition;
    private final Coord2D StartDirection;
    private final Map<Coord2D, MazeTile> Tiles;
    private final Integer ShortestDistance;
    Map<MazeTile, Set<MazeTile>> VisitedTilesOnTheWay = new HashMap<>();

    public ReindeerRaceMaze(String[] inputLines) {
        Map2D<Character> mazeAsArray = Map2D.fromStringInputLines(inputLines);
        Coord2D startTilePosition = null;
        Coord2D endTilePosition = null;
        Map<Coord2D, MazeTile> tiles = new HashMap<>();

        for (int rowIdx = 0; rowIdx <= mazeAsArray.MAX_ROW_INDEX; rowIdx++) {
            for (int colIdx = 0; colIdx <= mazeAsArray.MAX_COLUMN_INDEX; colIdx++) {
                Coord2D position = new Coord2D(rowIdx, colIdx);
                Character value = mazeAsArray.getAt(position);
                switch (value) {
                    case 'S':
                        startTilePosition = position;
                        tiles.put(position, MazeTile.fromPosition(position));
                        break;
                    case 'E':
                        endTilePosition = position;
                        tiles.put(position, MazeTile.fromPosition(position));
                        break;
                    case '.':
                        tiles.put(position, MazeTile.fromPosition(position));
                        break;
                }
            }
        }

        assert startTilePosition != null : "Could not find the start tile";
        assert endTilePosition != null : "Could not find the end tile";
        this.StartTilePosition = startTilePosition;
        this.EndTilePosition = endTilePosition;
        // The Reindeer start on the Start Tile (marked S) facing East
        this.StartDirection = RIGHT;
        this.Tiles = tiles;
        this.dijkstra();
        this.ShortestDistance = this.Tiles.get(this.EndTilePosition).getDistance();
    }

    public Long getBestPathScore() {
        return (long) this.ShortestDistance;
    }

    private void dijkstra() {
        MazeTile startTile = this.Tiles.get(this.StartTilePosition);
        startTile.setDistance(0);
        startTile.allPotentialPathEntries.add(startTile);

        Set<StackEntry> settledTiles = new HashSet<>();
        PriorityQueue<StackEntry> unsettledTiles = new PriorityQueue<>();

        unsettledTiles.add(new StackEntry(startTile, StartDirection));
        VisitedTilesOnTheWay.put(startTile, new HashSet<>());

        while (!unsettledTiles.isEmpty()) {
            StackEntry currentTile = unsettledTiles.remove();
            // Get all the possible next steps
            // Try continuing in the same direction or rotating 90 degrees clockwise and counterclockwise
            Coord2D currentDirection = currentTile.direction();
            Coord2D[] potentialDirections = {currentDirection, currentDirection.rotateRightAngle(90), currentDirection.rotateRightAngle(-90)};

            Coord2D currentPosition = currentTile.tile().getPosition();
            for (int i = 0; i < 3; i++) {
                Coord2D direction = potentialDirections[i];

                Coord2D nextTilePosition = currentPosition.add(direction);
                if (this.Tiles.containsKey(nextTilePosition)) {
                    MazeTile nextTile = this.Tiles.get(nextTilePosition);
                    StackEntry nextTileEntry = new StackEntry(nextTile, direction);

                    if (!settledTiles.contains(nextTileEntry)) {
                        unsettledTiles.add(nextTileEntry);
                        calculateMinimumDistance(nextTile, currentTile.tile(), direction);

                        VisitedTilesOnTheWay.merge(nextTile, new HashSet<>(Set.of(currentTile.tile())), (oldSet, newSet) -> {
                            oldSet.addAll(newSet);
                            return oldSet;
                        });
                        VisitedTilesOnTheWay.merge(nextTile, VisitedTilesOnTheWay.get(currentTile.tile()), (oldSet, newSet) -> {
                            oldSet.addAll(newSet);
                            return oldSet;
                        });
                    }
                }
            }
            settledTiles.add(currentTile);
        }
    }

    private void calculateMinimumDistance(MazeTile evaluationTile, MazeTile sourceTile, Coord2D direction) {
        int sourceDistance = sourceTile.getDistance();
        int newEdgeWeight = sourceDistance + 1;
        LinkedList<StackEntry> shortestPath = new LinkedList<>(sourceTile.getShortestPath());
        Coord2D lastDirection = !shortestPath.isEmpty() ? shortestPath.getLast().direction() : StartDirection;

        if (!direction.equals(lastDirection)) {
            newEdgeWeight = sourceDistance + 1001;
        }

        if (newEdgeWeight <= evaluationTile.getDistance()) {
            evaluationTile.setDistance(newEdgeWeight);
            shortestPath.add(new StackEntry(sourceTile, direction));
            evaluationTile.setShortestPath(shortestPath);
        }
    }
}

record StackEntry(MazeTile tile, Coord2D direction) implements Comparable<StackEntry> {
    @Override
    public int compareTo(StackEntry o) {
        return this.tile.compareTo(o.tile);
    }

    @Override
    public String toString() {
        return "StackEntry{" +
                "tile=" + tile.getPosition() +
                ", direction=" + DirectionSupport.directionToString(direction) +
                '}';
    }
}

package pl.apostaremczak.aoc;

import pl.apostaremczak.aoc.util.Coord2D;

import java.util.*;

public class Day18 extends PuzzleSolution {
    private final Integer MaxRowIdx;
    private final Integer MaxColumnIdx;
    private final Integer PartOneLimit;

    public Day18(String inputFilename, Integer maxRowIdx, Integer maxColumnIdx, Integer partOneLimit) {
        super(inputFilename);
        this.MaxRowIdx = maxRowIdx;
        this.MaxColumnIdx = maxColumnIdx;
        this.PartOneLimit = partOneLimit;
    }

    @Override
    public Long solvePart1() {
        NodeMaze maze = NodeMaze.fromInputLines(Arrays.copyOfRange(inputLines, 0, PartOneLimit), MaxRowIdx, MaxColumnIdx);
        maze.dijkstra();
        return (long) maze.distanceFromStartToEnd();
    }

    @Override
    public Long solvePart2() {
        for (int i = PartOneLimit; i < inputLines.length; i++) {
            NodeMaze maze = NodeMaze.fromInputLines(Arrays.copyOfRange(inputLines, 0, i + 1), MaxRowIdx, MaxColumnIdx);
            maze.dijkstra();
            if (!maze.doesPathExist()) {
                System.out.println(inputLines[i]);
                break;
            }
        }
        return 0L;
    }

    public static void main(String[] args) {
        Day18 day18 = new Day18("src/main/resources/18.txt", 70, 70, 1024);
        Long part1Solution = day18.solvePart1();
        System.out.println("Part 1: " + part1Solution);
        Long part2Solution = day18.solvePart2();
        System.out.println("Part 2: " + part2Solution);
    }
}

class Node implements Comparable<Node> {
    private final Coord2D position;

    private LinkedList<Node> shortestPath = new LinkedList<>();

    private Integer distance = Integer.MAX_VALUE;

    public Node(Coord2D position) {
        this.position = position;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer newDistance) {
        this.distance = newDistance;
    }

    public Coord2D getPosition() {
        return this.position;
    }

    public LinkedList<Node> getShortestPath() {
        return this.shortestPath;
    }

    public void setShortestPath(LinkedList<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.getDistance(), o.getDistance());
    }
}

class NodeMaze {
    private final Coord2D StartNodePosition;
    private final Coord2D EndNodePosition;
    public final Map<Coord2D, Node> Tiles;

    public static NodeMaze fromInputLines(String[] inputLines, Integer maxRowIdx, Integer maxColumnIdx) {
        Set<Coord2D> walls = new HashSet<>();
        for (String line : inputLines) {
            var coord = line.split(",");
            walls.add(new Coord2D(Integer.parseInt(coord[1]), Integer.parseInt(coord[0])));
        }
        Set<Coord2D> nodes = new HashSet<>();
        for (int rowIdx = 0; rowIdx <= maxRowIdx; rowIdx++) {
            for (int colIdx = 0; colIdx <= maxColumnIdx; colIdx++) {
                Coord2D pos = new Coord2D(rowIdx, colIdx);
                if (!walls.contains(pos)) {
                    nodes.add(pos);
                }
            }
        }

        return new NodeMaze(nodes, new Coord2D(0, 0), new Coord2D(maxRowIdx, maxColumnIdx));
    }

    public NodeMaze(Set<Coord2D> nodes, Coord2D start, Coord2D end) {
        Tiles = new HashMap<>();
        nodes.forEach(position -> {
            Tiles.put(position, new Node(position));
        });
        this.StartNodePosition = start;
        this.EndNodePosition = end;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int rowIdx = 0; rowIdx <= 6; rowIdx++) {
            for (int colIdx = 0; colIdx <= 6; colIdx++) {
                Coord2D pos = new Coord2D(rowIdx, colIdx);
                if (Tiles.containsKey(pos)) {
                    stringBuilder.append(".");
                } else {
                    stringBuilder.append("#");
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public void dijkstra() {
        Node startTile = this.Tiles.get(this.StartNodePosition);
        startTile.setDistance(0);

        Set<Node> settledTiles = new HashSet<>();
        PriorityQueue<Node> unsettledTiles = new PriorityQueue<>();

        unsettledTiles.add(Tiles.get(StartNodePosition));

        while (!unsettledTiles.isEmpty()) {
            Node currentNode = unsettledTiles.poll();
            unsettledTiles.remove(currentNode);

            Set<Coord2D> neighborPositions = currentNode.getPosition().getStraightSurrounding();
            for (Coord2D neighborPosition : neighborPositions) {
                if (Tiles.containsKey(neighborPosition)) {
                    Node nextNode = Tiles.get(neighborPosition);
                    if (!settledTiles.contains(nextNode)) {
                        unsettledTiles.add(nextNode);
                        calculateMinimumDistance(nextNode, currentNode);
                    }
                }
            }

            settledTiles.add(currentNode);
        }
    }

    private static void calculateMinimumDistance(Node evaluationTile, Node sourceTile) {
        int sourceDistance = sourceTile.getDistance();
        if (sourceDistance + 1 <= evaluationTile.getDistance()) {
            LinkedList<Node> shortestPath = new LinkedList<>(sourceTile.getShortestPath());
            evaluationTile.setDistance(sourceDistance + 1);
            shortestPath.add(sourceTile);
            evaluationTile.setShortestPath(shortestPath);
        }
    }

    public Integer distanceFromStartToEnd() {
        return Tiles.get(EndNodePosition).getDistance();
    }

    public boolean doesPathExist() {
        return Tiles.get(EndNodePosition).getDistance() < Integer.MAX_VALUE;
    }
}

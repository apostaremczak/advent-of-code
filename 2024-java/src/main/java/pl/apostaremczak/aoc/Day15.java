package pl.apostaremczak.aoc;

import pl.apostaremczak.aoc.util.Coord2D;
import pl.apostaremczak.aoc.util.DirectionSupport;
import pl.apostaremczak.aoc.util.Map2D;

import java.util.*;

public class Day15 extends PuzzleSolution implements DirectionSupport {
    public Day15(String inputFilename) {
        super(inputFilename);
    }

    public static void main(String[] args) {
        long startTotal = System.currentTimeMillis();
        Day15 day = new Day15("src/main/resources/15.txt");

        long startPart1 = System.currentTimeMillis();
        Long part1Solution = day.solvePart1();
        long endPart1 = System.currentTimeMillis();
        System.out.println("Part 1: " + part1Solution + " (Time: " + (endPart1 - startPart1) + " ms)");

        long startPart2 = System.currentTimeMillis();
        Long part2Solution = day.solvePart2();
        long endPart2 = System.currentTimeMillis();
        System.out.println("Part 2: " + part2Solution + " (Time: " + (endPart2 - startPart2) + " ms)");

        long endTotal = System.currentTimeMillis();
        System.out.println("Total time: " + (endTotal - startTotal) + " ms");
    }

    @Override
    public Long solvePart1() {
        var splitInput = this.fullRawInput.split("\n\n");

        String[] mapRepresentationLines = splitInput[0].split("\n");
        Map2D<Character> mapAsArray = Map2D.fromStringInputLines(mapRepresentationLines);
        Coord2D robotPosition = null;

        Set<Coord2D> wallPositions = new HashSet<>();
        Set<Coord2D> boxPositions = new HashSet<>();

        for (int rowIdx = 0; rowIdx <= mapAsArray.MAX_ROW_INDEX; rowIdx++) {
            for (int colIdx = 0; colIdx <= mapAsArray.MAX_COLUMN_INDEX; colIdx++) {
                Coord2D position = new Coord2D(rowIdx, colIdx);
                Character value = mapAsArray.getAt(position);
                switch (value) {
                    case '@':
                        robotPosition = position;
                        break;
                    case '#':
                        wallPositions.add(position);
                        break;
                    case 'O':
                        boxPositions.add(position);
                        break;
                }
            }
        }
        assert robotPosition != null : "Did not find the robot's initial position";
        List<Coord2D> movements = splitInput[1].strip().chars().mapToObj(c -> (char) c).filter(c -> c != '\n').map(this::parseMovement).toList();

        robotMovementLoop:
        for (Coord2D movementDirection : movements) {
            Coord2D nextRobotPosition = robotPosition.add(movementDirection);
            // Check for the simplest case when the next spot is empty
            if (!wallPositions.contains(nextRobotPosition) && !boxPositions.contains(nextRobotPosition)) {
                robotPosition = nextRobotPosition;
                continue;
            }

            // Get a LIFO list of objects to be moved;
            LinkedList<Coord2D> objectsToBeMoved = new LinkedList<>();
            Coord2D currentlyInvestigated = nextRobotPosition;
            boolean finishedInvestigating = wallPositions.contains(currentlyInvestigated);
            if (finishedInvestigating) {
                continue;
            }

            while (!finishedInvestigating) {
                objectsToBeMoved.add(currentlyInvestigated);
                currentlyInvestigated = currentlyInvestigated.add(movementDirection);

                // If a wall is encountered, stop the entire operation as nothing can be moved
                if (wallPositions.contains(currentlyInvestigated)) {
                    continue robotMovementLoop;
                }
                // Finish once the next spot is unoccupied
                finishedInvestigating = !boxPositions.contains(currentlyInvestigated);
            }

            // Move if possible
            while (!objectsToBeMoved.isEmpty()) {
                Coord2D currentBoxPosition = objectsToBeMoved.removeLast();
                Coord2D movedBoxPosition = currentBoxPosition.add(movementDirection);
                assert !wallPositions.contains(movedBoxPosition) : "Somehow tried to move a box into the wall";
                boxPositions.remove(currentBoxPosition);
                boxPositions.add(movedBoxPosition);
            }

            robotPosition = nextRobotPosition;
        }

        return boxPositions.stream().map(this::getGpsCoordinate).mapToLong(l -> l).sum();
    }

    private Coord2D parseMovement(Character movement) {
        return switch (movement) {
            case '<' -> LEFT;
            case '>' -> RIGHT;
            case 'v' -> BOTTOM;
            case '^' -> TOP;
            default -> throw new IllegalArgumentException("Incorrect movement : " + movement);
        };
    }

    /**
     * The GPS coordinate of a box is equal to 100 times its distance from the top edge
     * of the map plus its distance from the left edge of the map.
     */
    private Long getGpsCoordinate(Coord2D boxPosition) {
        // 100 times its distance from the top edge =>
        // 100 * row index
        // Distance from the left edge of the map => column index
        return 100L * boxPosition.row() + boxPosition.column();
    }

    @Override
    public Long solvePart2() {
        var splitInput = this.fullRawInput.split("\n\n");
        String modifiedRepresentation = splitInput[0]
                .replaceAll("#", "##")
                .replaceAll("O", "[]")
                .replaceAll("\\.", "..")
                .replaceAll("@", "@.");

        String[] mapRepresentationLines = modifiedRepresentation.split("\n");

        Map2D<Character> mapAsArray = Map2D.fromStringInputLines(mapRepresentationLines);
        Coord2D robotPosition = null;
        Set<Coord2D> wallPositions = new HashSet<>();
        Map<Coord2D, Box> boxMap = new HashMap<>();

        for (int rowIdx = 0; rowIdx <= mapAsArray.MAX_ROW_INDEX; rowIdx++) {
            for (int colIdx = 0; colIdx <= mapAsArray.MAX_COLUMN_INDEX; colIdx++) {
                Coord2D position = new Coord2D(rowIdx, colIdx);
                Character value = mapAsArray.getAt(position);
                switch (value) {
                    case '@':
                        robotPosition = position;
                        break;
                    case '#':
                        wallPositions.add(position);
                        break;
                    case '[':
                        Box box = new Box(position);
                        boxMap.put(box.leftSide(), box);
                        boxMap.put(box.rightSide(), box);
                        break;
                }
            }
        }
        assert robotPosition != null : "Did not find the robot's initial position";

        List<Coord2D> movements = splitInput[1].strip().chars().mapToObj(c -> (char) c).filter(c -> c != '\n').map(this::parseMovement).toList();
        robotMovementLoop:
        for (Coord2D movementDirection : movements) {
            Coord2D nextRobotPosition = robotPosition.add(movementDirection);
            // Check for the simplest case when the next spot is empty
            if (!wallPositions.contains(nextRobotPosition) && !boxMap.containsKey(nextRobotPosition)) {
                robotPosition = nextRobotPosition;
                continue;
            }

            if (wallPositions.contains(nextRobotPosition)) {
                continue;
            }

            // Get a LIFO list of objects to be moved;
            LinkedList<Box> objectsToBeMoved = new LinkedList<>();

            LinkedList<Coord2D> toBeInvestigated = new LinkedList<>();
            toBeInvestigated.add(nextRobotPosition);
            Set<Coord2D> alreadyInvestigated = new HashSet<>();

            while (!toBeInvestigated.isEmpty()) {
                Coord2D currentlyInvestigated = toBeInvestigated.removeFirst();
                alreadyInvestigated.add(currentlyInvestigated);

                // If a wall is encountered, stop the entire operation as nothing can be moved
                if (wallPositions.contains(currentlyInvestigated)) {
                    continue robotMovementLoop;
                }

                // If an empty spot is encountered, no new position will be examined
                if (!boxMap.containsKey(currentlyInvestigated)) {
                    continue;
                }

                Box boxToBeMoved = boxMap.get(currentlyInvestigated);
                if (!objectsToBeMoved.contains(boxToBeMoved)) {
                    objectsToBeMoved.add(boxToBeMoved);
                }

                if (!alreadyInvestigated.contains(boxToBeMoved.leftSide())) {
                    toBeInvestigated.add(boxToBeMoved.leftSide());
                }
                if (!alreadyInvestigated.contains(boxToBeMoved.rightSide())) {
                    toBeInvestigated.add(boxToBeMoved.rightSide());
                }
                toBeInvestigated.add(currentlyInvestigated.add(movementDirection));
            }

            while (!objectsToBeMoved.isEmpty()) {
                Box currentlyExaminedBox = objectsToBeMoved.removeLast();
                Box movedBox = currentlyExaminedBox.move(movementDirection);
                assert !wallPositions.contains(movedBox.leftSide()) && !wallPositions.contains(movedBox.rightSide()) : "Somehow tried to move a box into the wall";

                boxMap.remove(currentlyExaminedBox.leftSide());
                boxMap.remove(currentlyExaminedBox.rightSide());
                boxMap.put(movedBox.leftSide(), movedBox);
                boxMap.put(movedBox.rightSide(), movedBox);
            }

            robotPosition = nextRobotPosition;
        }

        return new HashSet<>(boxMap.values()).stream().map(Box::gpsCoordinate).mapToLong(l -> l).sum();
    }
}

record Box(Coord2D leftSide) implements DirectionSupport {
    public Box move(Coord2D direction) {
        return new Box(leftSide.add(direction));
    }

    public Long gpsCoordinate() {
        return 100L * leftSide.row() + leftSide.column();
    }

    public Coord2D rightSide() {
        return leftSide.add(RIGHT);
    }
}

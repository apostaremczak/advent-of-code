package pl.apostaremczak.aoc;

import org.apache.commons.collections4.iterators.PermutationIterator;
import pl.apostaremczak.aoc.util.Coord2D;
import pl.apostaremczak.aoc.util.DirectionSupport;

import java.util.*;

public class Day21 extends PuzzleSolution {
    private final Map<CacheKey, Long> ShortestMovesCache = new HashMap<>();

    public Day21(String inputFilename) {
        super(inputFilename);
    }

    @Override
    public Long solvePart1() {
        return solveForNRobots(3);
    }

    @Override
    public Long solvePart2() {
        return solveForNRobots(26);
    }

    private Long solveForNRobots(Integer numberOfRobots) {
        long result = 0;
        for (String code : inputLines) {
            long minValue = getShortestMoveLength(Keypad.buildNumeric(), code, numberOfRobots);
            int numericValue = Integer.parseInt(code.substring(0, code.length() - 1));
            result += minValue * numericValue;
        }
        return result;
    }

    public static void main(String[] args) {
        Day21 day21 = new Day21("src/main/resources/21.txt");
        Long part1Solution = day21.solvePart1();
        System.out.println("Part 1: " + part1Solution);
        Long part2Solution = day21.solvePart2();
        System.out.println("Part 2: " + part2Solution);
    }

    // Great caching technique from https://www.reddit.com/r/adventofcode/comments/1hj2odw/comment/m343kus/
    public Long getShortestMoveLength(Keypad keypad, String code, Integer numberOfRobots) {
        CacheKey cacheKey = new CacheKey(keypad.size(), code, numberOfRobots);
        if (ShortestMovesCache.containsKey(cacheKey)) {
            return ShortestMovesCache.get(cacheKey);
        }

        if (numberOfRobots == 0) {
            ShortestMovesCache.put(cacheKey, (long) code.length());
            return (long) code.length();
        }

        long minimalLength = 0;
        int numberOfNewRobots = numberOfRobots - 1;
        Character currentButton = 'A';
        for (Character destinationButton : code.toCharArray()) {
            Set<String> possibleMoves = keypad.allMovesBetweenButtons(currentButton, destinationButton);
            minimalLength += possibleMoves.stream().map(moveChars ->
                    getShortestMoveLength(Keypad.buildDirectional(), moveChars, numberOfNewRobots)
            ).mapToLong(i -> i).min().getAsLong();

            currentButton = destinationButton;
        }

        ShortestMovesCache.put(cacheKey, minimalLength);
        return minimalLength;
    }

    private record CacheKey(Integer KeypadLength, String code, Integer numberOfRobots) {
    }
}

class Keypad {
    private final Map<Character, Coord2D> Buttons = new HashMap<>();
    public static Character EmptyMove = ' ';
    public static Character[][] NumericButtons = {{'7', '8', '9'}, {'4', '5', '6'}, {'1', '2', '3'}, {Keypad.EmptyMove, '0', 'A'}};
    public static Character[][] DirectionalButtons = {{Keypad.EmptyMove, '^', 'A'}, {'<', 'v', '>'}};
    private final Coord2D EmptySpotLocation;

    public Keypad(Character[][] buttons) {
        for (int rowIdx = 0; rowIdx < buttons.length; rowIdx++) {
            for (int columnIdx = 0; columnIdx < buttons[rowIdx].length; columnIdx++) {
                Character button = buttons[rowIdx][columnIdx];
                Coord2D coord = new Coord2D(rowIdx, columnIdx);
                Buttons.put(button, coord);
            }
        }
        EmptySpotLocation = Buttons.get(EmptyMove);
    }

    public Integer size() {
        return Buttons.size();
    }

    public static Keypad buildNumeric() {
        return new Keypad(NumericButtons);
    }

    public static Keypad buildDirectional() {
        return new Keypad(DirectionalButtons);
    }

    public Set<String> allMovesBetweenButtons(Character fromChar, Character toChar) {
        Coord2D from = Buttons.get(fromChar);
        Coord2D to = Buttons.get(toChar);
        Coord2D move = to.minus(from);
        Set<String> representations = new HashSet<>();

        // Horizontal moves
        int numHorizontalSymbols = Math.abs(move.column());
        char horizontalSymbol;
        if (move.column() < 0) {
            // Moving left
            horizontalSymbol = '<';
        } else {
            // Moving right
            horizontalSymbol = '>';
        }

        // Vertical moves
        int numVerticalSymbols = Math.abs(move.row());
        char verticalSymbol;
        if (move.row() < 0) {
            // Going up
            verticalSymbol = '^';
        } else {
            verticalSymbol = 'v';
        }

        // numVerticalSymbols repetitions of verticalSymbol and numHorizontalSymbols repetitions of horizontalSymbol
        List<Character> availableCharsRepeated = new ArrayList<>();
        for (int i = 0; i < numVerticalSymbols; i++) {
            availableCharsRepeated.add(verticalSymbol);
        }
        for (int i = 0; i < numHorizontalSymbols; i++) {
            availableCharsRepeated.add(horizontalSymbol);
        }

        // Create a list of all possible representations: do all combinations of vertical and horizontal symbols
        PermutationIterator<Character> permutations = new PermutationIterator<>(availableCharsRepeated);
        while (permutations.hasNext()) {
            List<Character> permutation = permutations.next();
            StringBuilder representation = new StringBuilder();
            for (Character c : permutation) {
                representation.append(c);
            }
            representations.add(representation.toString());
        }

        Set<String> legalMoves = new HashSet<>();

        // Make sure that the robot does not pass through the empty spot on the way to the target
        legalMoveSearch:
        for (String potentialMove : representations) {
            List<Character> moveChars = potentialMove.chars().mapToObj(c -> (char) c).toList();
            Coord2D pos = new Coord2D(from.row(), from.column());
            for (Character moveChar : moveChars) {
                pos = switch (moveChar) {
                    case '^' -> pos.add(DirectionSupport.TOP);
                    case '>' -> pos.add(DirectionSupport.RIGHT);
                    case 'v' -> pos.add(DirectionSupport.BOTTOM);
                    case '<' -> pos.add(DirectionSupport.LEFT);
                    default -> throw new IllegalStateException("Unexpected value: " + moveChar);
                };
                if (pos.equals(EmptySpotLocation)) {
                    continue legalMoveSearch;
                }
            }

            // Push the button
            legalMoves.add(potentialMove + "A");
        }

        // Give priority to moves that do not change the direction so often (e.g. '>^>' should be dropped in favor of '>>^'
        Map<String, Integer> directionChangeCount = new HashMap<>();
        for (String legalMove : legalMoves) {
            int directionChanges = 0;
            for (int i = 0; i < legalMove.length() - 1; i++) {
                if (legalMove.charAt(i) != legalMove.charAt(i + 1)) {
                    directionChanges++;
                }
            }
            directionChangeCount.put(legalMove, directionChanges);
        }
        int fewestDirectionChanges = directionChangeCount.values().stream().min(Integer::compareTo).get();

        Set<String> legalMovesWithFewestDirectionChanges = new HashSet<>();
        for (Map.Entry<String, Integer> entry : directionChangeCount.entrySet()) {
            if (entry.getValue() == fewestDirectionChanges) {
                legalMovesWithFewestDirectionChanges.add(entry.getKey());
            }
        }

        return legalMovesWithFewestDirectionChanges;
    }
}

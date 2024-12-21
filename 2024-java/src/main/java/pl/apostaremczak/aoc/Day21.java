package pl.apostaremczak.aoc;

import org.apache.commons.collections4.iterators.PermutationIterator;
import pl.apostaremczak.aoc.util.Coord2D;
import pl.apostaremczak.aoc.util.DirectionSupport;

import java.util.*;

public class Day21 extends PuzzleSolution {
    public Day21(String inputFilename) {
        super(inputFilename);
    }

    @Override
    public Long solvePart1() {
        // In summary, there are the following keypads:
        Keypad myKeypad = Keypad.buildDirectional();
        Keypad robotKeyboard = Keypad.buildDirectional();
        Keypad doorRobotKeypad = Keypad.buildNumeric();

        long result = 0L;
        for (String code : inputLines) {
            List<Character> doorCodeProgram = code.chars().mapToObj(c -> (char) c).toList();
            // For each character in the program, find the shortest possible path to the button
            for (Character destination : doorCodeProgram) {
                Coord2D from = doorRobotKeypad.CurrentPosition;
                Coord2D doorRobotMove = doorRobotKeypad.moveTo(destination);
                Set<String> potentialDoorRobotSteps = doorRobotKeypad.moveToVisualRepresentations(doorRobotMove, from);
                for (String doorRobotSteps : potentialDoorRobotSteps) {

                }
            }

            List<Character> doorRobotKeypadMoves = doorRobotKeypad.findMovesForProgram(doorCodeProgram);
            List<Character> robotMoves = robotKeyboard.findMovesForProgram(doorRobotKeypadMoves);
            List<Character> myMoves = myKeypad.findMovesForProgram(robotMoves);
            String myMovesString = Keypad.movesToString(myMoves);
            System.out.println(myMovesString);
            long numericPartOfCode = Long.parseLong(code.substring(0, code.length() - 1));
            result += (numericPartOfCode * myMovesString.length());
            System.out.println(myMovesString.length() + " * " + numericPartOfCode);
        }
        return result;
    }

    public String findMyMoves(Character codeLetter) {
        Keypad myKeypad = Keypad.buildDirectional();
        Keypad robotKeyboard = Keypad.buildDirectional();
        Keypad doorRobotKeypad = Keypad.buildNumeric();

        List<Character> doorRobotKeypadMoves = doorRobotKeypad.findMovesForProgram(List.of(codeLetter));
        List<Character> robotMoves = robotKeyboard.findMovesForProgram(doorRobotKeypadMoves);
        List<Character> myMoves = myKeypad.findMovesForProgram(robotMoves);
        return Keypad.movesToString(myMoves);
    }

    public String findShortestMovesForProgram(List<Character> program, LinkedList<Keypad> downstreamRobotKeypads) {
        Keypad keypad = downstreamRobotKeypads.removeFirst();

        if (downstreamRobotKeypads.isEmpty()) {
            List<Character> moves = keypad.findMovesForProgram(program);
            return Keypad.movesToString(moves);
        }

        return "f";
    }

    @Override
    public Long solvePart2() {
        return 0L;
    }

    public static void main(String[] args) {
        Day21 day21 = new Day21("src/main/resources/21.txt");
        Long part1Solution = day21.solvePart1();
        System.out.println("Part 1: " + part1Solution);
        Long part2Solution = day21.solvePart2();
        System.out.println("Part 2: " + part2Solution);
    }
}

class Keypad {
    public final Map<Character, Coord2D> Buttons = new HashMap<>();
    private Coord2D StartPosition;
    public Coord2D CurrentPosition;
    public static Character EmptyMove = ' ';
    public static Character[][] NumericButtons = {{'7', '8', '9'}, {'4', '5', '6'}, {'1', '2', '3'}, {Keypad.EmptyMove, '0', 'A'}};
    public static Character[][] DirectionalButtons = {{Keypad.EmptyMove, '^', 'A'}, {'<', 'v', '>'}};
    public Coord2D EmptySpotLocation;

    public Keypad(Character[][] buttons) {
        for (int rowIdx = 0; rowIdx < buttons.length; rowIdx++) {
            for (int columnIdx = 0; columnIdx < buttons[rowIdx].length; columnIdx++) {
                Character button = buttons[rowIdx][columnIdx];
                Coord2D coord = new Coord2D(rowIdx, columnIdx);
                Buttons.put(button, coord);
                if (button == 'A') {
                    StartPosition = coord;
                }
            }
        }

        CurrentPosition = StartPosition;
        EmptySpotLocation = Buttons.get(EmptyMove);
    }

    public static Keypad buildNumeric() {
        return new Keypad(NumericButtons);
    }

    public static Keypad buildDirectional() {
        return new Keypad(DirectionalButtons);
    }

    public Coord2D moveTo(Coord2D to) {
        Coord2D move = to.minus(CurrentPosition);
        CurrentPosition = to;
        return move;
    }

    public Coord2D moveTo(Character to) {
        Coord2D endPosition = Buttons.get(to);
        return moveTo(endPosition);
    }

    public Set<String> moveToVisualRepresentations(Coord2D move, Coord2D from) {
        Set<String> representations = new HashSet<>();

        // Horizontal moves
        int numHorizontalSymbols = Math.abs(move.column());
        char horizontalSymbol = EmptyMove;
        if (move.column() < 0) {
            // Moving left
            horizontalSymbol = '<';
        } else {
            // Moving right
            horizontalSymbol = '>';
        }

        // Vertical moves
        int numVerticalSymbols = Math.abs(move.row());
        char verticalSymbol = EmptyMove;
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

        return legalMoves;
    }

    public List<Character> findMovesForProgram(List<Character> program) {
        List<Character> representation = new ArrayList<>();
        for (Character destination : program) {
            Coord2D move = moveTo(destination);
//            representation.addAll(moveToVisualRepresentations(move).stream().toList().getFirst().chars().mapToObj(c -> (char) c).toList());
        }
        returnToStart();
        return representation;
    }

    // Move the robot hand back to the initial position
    private void returnToStart() {
        CurrentPosition = StartPosition;
    }

    public static String movesToString(List<Character> moves) {
        StringBuilder rep = new StringBuilder();
        for (Character move : moves) {
            rep.append(move);
        }
        return rep.toString();
    }
}

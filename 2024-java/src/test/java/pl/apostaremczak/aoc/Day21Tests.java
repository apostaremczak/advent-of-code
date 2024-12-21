package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;
import pl.apostaremczak.aoc.util.Coord2D;

import java.security.Key;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day21Tests {
    private final Day21 day21 = new Day21("src/test/resources/21.txt");

    @Test
    public void testSolvePart1() {
        Long result = day21.solvePart1();
        assertEquals(126384L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day21.solvePart2();
        assertEquals(0L, result);
    }

    @Test
    public void testMoveToVisualRepresentations() {
        Keypad numericKeypad = Keypad.buildNumeric();
        Set<String> possibleMoves = Set.of(">^^A", "^>^A", "^^>A");
        Set<String> result = numericKeypad.moveToVisualRepresentations(new Coord2D(-2, 1), numericKeypad.Buttons.get('2'));
        assertEquals(possibleMoves, result);
    }

    @Test
    public void testNumericKeypad() {
        Keypad numericKeypad = Keypad.buildNumeric();
        Character[] program = {'0', '2', '9', 'A'};
        List<Character> result = numericKeypad.findMovesForProgram(List.of(program));
        String resultString = Keypad.movesToString(result);
        assertEquals("<A^A>^^AvvvA", resultString);
    }

    @Test
    public void testDirectionalKeypad() {
        Keypad directionalKeypad = Keypad.buildDirectional();
        Character[] program = {'<', 'A', '^', 'A', '>', '^', '^', 'A', 'v', 'v', 'v', 'A'};
        List<Character> result = directionalKeypad.findMovesForProgram(List.of(program));
        String resultString = Keypad.movesToString(result);
        assertEquals("<<vA>>^A<A>AvA<^AA>A<vAAA>^A", resultString);
    }

    @Test
    public void testDirectionalKeypad2() {
        Keypad directionalKeypad = Keypad.buildDirectional();
        Character[] program = {'v', '<', '<', 'A', '>', '>', '^', 'A', '<', 'A', '>', 'A', 'v', 'A', '<', '^', 'A', 'A', '>', 'A', '<', 'v', 'A', 'A', 'A', '>', '^', 'A'};
        List<Character> result = directionalKeypad.findMovesForProgram(List.of(program));
        String resultString = Keypad.movesToString(result);
        assertEquals("<vA<AA>>^AvAA<^A>A<<vA>>^AvA^A<vA>^A<<vA>^A>AAvA^A<<vA>A>^AAAvA<^A>A", resultString);
    }
//
//    @Test
//    public void testFindMyMoves() {
//        String code = "379A";
//        String result = day21.findMyMoves(code);
//        assertEquals("v<<A^>>AvA^Av<A<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A", result);
//    }
}

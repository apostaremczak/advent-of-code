from solutions.day_10_syntax_scorring import *


def test_get_corruption_score():
    assert get_corruption_score("()") == 0
    assert get_corruption_score("{()()()}") == 0
    assert get_corruption_score("(((((((((())))))))))") == 0

    assert get_corruption_score("(]") > 0
    assert get_corruption_score("<([]){()}[{}])") > 0

    # Corrupted lines
    assert get_corruption_score("{([(<{}[<>[]}>{[]{[(<()>") == 1197
    assert get_corruption_score("[[<[([]))<([[{}[[()]]]") == 3
    assert get_corruption_score("[<(<(<(<{}))><([]([]()") == 3
    assert get_corruption_score("[{[{({}]{}}([{[{{{}}([]") == 57
    assert get_corruption_score("<{([([[(<>()){}]>(<<{{") == 25137

    # Incomplete lines
    assert get_corruption_score("[({(<(())[]>[[{[]{<()<>>") == 0
    assert get_corruption_score("[(()[<>])]({[<{<<[]>>(") == 0


def test_part_1():
    assert part_1(read_puzzle_input("puzzle_inputs/day_10.txt")) == 26397


def test_get_completion_score():
    assert get_completion_score("])}>") == 294
    assert get_completion_score("}}]])})]") == 288957
    assert get_completion_score("}}>}>))))") == 1480781

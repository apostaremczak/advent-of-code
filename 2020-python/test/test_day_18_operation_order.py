from solutions.day_18_operation_order import *


def test_evaluate_flat():
    assert evaluate_flat("1 + 2 * 3 + 4 * 5 + 6") == 71
    assert evaluate_flat("4 * 11") == 44


def test_find_outermost_parentheses_content():
    exp_1 = "1 + (2 * (3 + 4)) + (4 * (5 + 6))"
    assert find_first_parentheses(exp_1) == "(2 * (3 + 4))"
    exp_2 = "2 * (3 + 4)"
    assert find_first_parentheses(exp_2) == "(3 + 4)"


def test_evaluate():
    assert evaluate("1 + 2 * 3 + 4 * 5 + 6") == 71
    assert evaluate("4 * (5 + 6)") == 44
    assert evaluate("1 + (2 * 3) + (4 * (5 + 6))") == 51
    assert evaluate("5 + (8 * 3 + 9 + 3 * 4 * 3)") == 437
    assert evaluate("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))") == 12240
    assert evaluate("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2") == 13632


def test_evaluate_flat_addition_first():
    assert evaluate_flat_addition_first("1 + 2 * 3 + 4 * 5 + 6") == 231


def test_part_1():
    entries = [
        "2 * 3 + (4 * 5)",
        "5 + (8 * 3 + 9 + 3 * 4 * 3)",
        "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"
    ]
    assert part_1(entries) == 26 + 437 + 12240


def test_part_2():
    entries = [
        "1 + (2 * 3) + (4 * (5 + 6))",
        "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))",
        "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"
    ]
    assert part_2(entries) == 51 + 669060 + 23340

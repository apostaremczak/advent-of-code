import re
from collections import Counter
from typing import Callable, List


def read_puzzle_input(input_file_path: str) -> List[str]:
    with open(input_file_path, "r") as f:
        return [x.strip() for x in f.readlines()]


def evaluate_flat(expression: str) -> int:
    matches = re.match(r"^(\d+ [+|*] \d+)", expression)
    if not matches:
        return int(expression)
    op = matches.group(1)
    op_replaced = expression.replace(op, str(eval(op)), 1)
    return evaluate_flat(op_replaced)


def evaluate_flat_addition_first(expression: str) -> int:
    matches = re.search(r"(\d+ \+ \d+)", expression)
    if not matches:
        return evaluate_flat(expression)
    op = matches.group()
    op_replaced = expression.replace(op, str(eval(op)), 1)
    return evaluate_flat_addition_first(op_replaced)


def find_first_parentheses(expression: str) -> str:
    first_loc = expression.find("(")
    counts = Counter()
    for i, char in enumerate(expression[first_loc:], start=first_loc):
        if char in ("(", ")"):
            counts.update(char)
            if counts["("] == counts[")"]:
                return expression[first_loc:i + 1]


def evaluate(expression: str,
             evaluate_flat_fn: Callable[[str], int] = evaluate_flat) -> int:
    if not expression.count("("):
        return evaluate_flat_fn(expression)
    parentheses = find_first_parentheses(expression)
    result = evaluate(parentheses[1:-1], evaluate_flat_fn)
    op_replaced = expression.replace(parentheses, str(result), 1)
    return evaluate(op_replaced, evaluate_flat_fn)


def part_1(expressions: List[str]) -> int:
    return sum(evaluate(x) for x in expressions)


def part_2(expressions: List[str]) -> int:
    return sum(evaluate(x, evaluate_flat_addition_first) for x in expressions)


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_18.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")

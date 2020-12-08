from solutions.day_08_handheld_halting import *


def test_part_1():
    rules = read_puzzle_input("puzzle_inputs/day_08.txt")
    assert part_1(rules) == 5


def test_part_2():
    rules = read_puzzle_input("puzzle_inputs/day_08.txt")
    assert part_2(rules) == 8
